package com.companion.lol.data.usecase

import com.companion.lol.data.mapper.model
import com.companion.lol.data.model.ChampionDetailsModel
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.data.model.ChampionWithDetailsModel
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SkinStore
import com.companion.lol.storage.impl.util.dbDispatcher
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import com.companion.lol.util.listMap
import com.companion.lol.util.withRetry
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@Singleton
class ChampionUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val favoritesStore: ChampionFavoritesStore,
  private val skinsStore: SkinStore,
  private val championDetailsStore: ChampionDetailsStore,
  private val refreshChampionDetailsUseCase: RefreshChampionDetailsUseCase,
) {
  fun observeAllWithFavorites(): Flow<List<ChampionModel>> {
    return championStore
      .observeAllWithFavorites(dbDispatcher)
      .listMap(ChampionWithFavoritesView::model)
  }

  suspend fun markFavourite(championId: ChampionId, isFavourite: Boolean) = withDbContext {
    favoritesStore.markFavorite(championId = championId, isFavorite = isFavourite)
  }

  suspend fun clearFavorites() = withDbContext { favoritesStore.clearAll() }

  fun observeChampionWithDetails(
    championId: ChampionId,
    refetch: Boolean,
  ): Flow<ChampionWithDetailsModel> =
    flow {
        val keyName = checkNotNull(championStore.findKeyNameById(championId))

        if (refetch) {
          coroutineScope {
            launch {
              // this should be in ViewModel not here..
              // TODO: do the proper way
              withRetry(times = 12, delayDuration = 5.seconds) {
                  refreshChampionDetailsUseCase.refresh(championId, keyName)
                }
                .onFailure { Timber.e(it) }
            }
          }

          emitAll(
            combine(
              championStore.observeWithFavoritesById(championId, dbDispatcher).map { it.model() },
              observeChampionDetails(championId, keyName),
            ) { champion, details ->
              ChampionWithDetailsModel(champion = champion, details = details)
            }
          )
        }
      }
      .flowOn(dbDispatcher)

  private fun observeChampionDetails(
    championId: ChampionId,
    keyName: String,
  ): Flow<ChampionDetailsModel?> =
    combine(
      championDetailsStore.observeByID(championId, dbDispatcher),
      skinsStore.observeByChampionId(championId, dbDispatcher),
    ) { details, skins ->
      details?.model(keyName, skins)
    }
}
