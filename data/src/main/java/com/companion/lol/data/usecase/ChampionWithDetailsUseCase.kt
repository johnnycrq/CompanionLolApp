package com.companion.lol.data.usecase

import com.companion.lol.data.mapper.model
import com.companion.lol.data.model.ChampionDetailsModel
import com.companion.lol.data.model.ChampionWithDetailsModel
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SkinStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.impl.util.dbDispatcher
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@Singleton
class ChampionWithDetailsUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val skinsStore: SkinStore,
  private val championDetailsStore: ChampionDetailsStore,
  private val dispatcher: DbDispatcher,
) {
  fun observeChampionWithDetails(championId: ChampionId): Flow<ChampionWithDetailsModel> =
    flow {
        val keyName = checkNotNull(championStore.findKeyNameById(championId))

        emitAll(
          combine(
            championStore.observeWithFavoritesById(championId, dbDispatcher).map { it.model() },
            observeChampionDetails(championId, keyName),
          ) { champion, details ->
            ChampionWithDetailsModel(champion = champion, details = details)
          }
        )
      }
      .flowOn(dispatcher)

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
