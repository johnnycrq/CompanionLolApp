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
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import com.companion.lol.util.listMap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionUseCase @Inject constructor(
    private val championStore: ChampionStore,
    private val favoritesStore: ChampionFavoritesStore,
    private val skinsStore: SkinStore,
    private val championDetailsStore: ChampionDetailsStore,
    private val refreshChampionDetailsUseCase: RefreshChampionDetailsUseCase,
) {
    fun observeAllWithFavorites(): Flow<List<ChampionModel>>{
        return championStore.observeAllWithFavorites()
            .listMap(ChampionWithFavoritesView::model)
    }
    fun observeWithFavoritesById(championId: ChampionId): Flow<ChampionModel>{
        return championStore.observeWithFavoritesById(championId)
            .map(ChampionWithFavoritesView::model)
    }
    suspend fun markFavourite(championId: ChampionId, isFavourite: Boolean) = withDbContext{
        favoritesStore.markFavorite(championId = championId, isFavorite = isFavourite)
    }

    suspend fun clearFavorites() = withDbContext{
        favoritesStore.clearAll()
    }
    fun observeChampionWithDetails(championId: ChampionId): Flow<ChampionWithDetailsModel> = flow {
        val keyName = checkNotNull(championStore.findKeyNameById(championId))

        coroutineScope {
            launch {
                try {
                    refreshChampionDetailsUseCase.refresh(championId, keyName)
                } catch (e: Exception) {
                    // Ignore refresh errors to continue observing cached data
                }
            }
        }

        emitAll(
            combine(
                championStore.observeWithFavoritesById(championId).map { it.model() },
                observeChampionDetails(championId, keyName),
            ) { champion, details ->
                ChampionWithDetailsModel(champion = champion, details = details)
            }
        )
    }

    private fun observeChampionDetails(
        championId: ChampionId,
        keyName: String,
    ): Flow<ChampionDetailsModel?> =
        combine(
            championDetailsStore.observeByID(championId),
            skinsStore.observeByChampionId(championId),
        ) { details, skins ->
            details?.model(keyName, skins)
        }
}