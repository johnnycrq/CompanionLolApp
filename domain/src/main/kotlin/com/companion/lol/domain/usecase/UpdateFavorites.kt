package com.companion.lol.domain.usecase

import com.companion.lol.core.model.ChampionId
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import javax.inject.Inject

class UpdateFavorites @Inject constructor(private val favoritesStore: ChampionFavoritesStore) {
  suspend operator fun invoke(championId: ChampionId, isFavorite: Boolean) {
    favoritesStore.markFavorite(championId = championId, isFavorite = isFavorite)
  }
}
