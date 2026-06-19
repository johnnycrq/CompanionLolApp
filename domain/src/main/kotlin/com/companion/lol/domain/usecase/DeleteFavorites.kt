package com.companion.lol.domain.usecase

import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import javax.inject.Inject

class DeleteFavorites @Inject constructor(private val favoritesStore: ChampionFavoritesStore) {
  suspend operator fun invoke() = favoritesStore.clearAll()
}
