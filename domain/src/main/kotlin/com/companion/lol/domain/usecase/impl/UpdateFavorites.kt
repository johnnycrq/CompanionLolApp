package com.companion.lol.domain.usecase.impl

import com.companion.lol.core.model.ChampionId
import com.companion.lol.domain.usecase.UpdateFavorites
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import javax.inject.Inject

class UpdateFavoritesImpl @Inject constructor(private val favoritesStore: ChampionFavoritesStore) :
  UpdateFavorites {
  override suspend operator fun invoke(championId: ChampionId, isFavorite: Boolean) {
    favoritesStore.markFavorite(championId = championId, isFavorite = isFavorite)
  }
}
