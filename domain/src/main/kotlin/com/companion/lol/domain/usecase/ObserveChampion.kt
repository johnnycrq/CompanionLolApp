package com.companion.lol.domain.usecase

import com.companion.lol.domain.mapper.model
import com.companion.lol.domain.model.Champion
import com.companion.lol.domain.util.listMap
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveChampion @Inject constructor(private val championStore: ChampionStore) {
  operator fun invoke(): Flow<List<Champion>> =
    championStore.observeAllWithFavorites().listMap(ChampionWithFavoritesView::model)
}
