package com.companion.lol.domain.usecase.impl

import com.companion.lol.domain.usecase.DeleteFavorites
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import javax.inject.Inject

class DeleteFavoritesImpl @Inject constructor(private val favoritesStore: ChampionFavoritesStore) :
  DeleteFavorites {
  override suspend operator fun invoke() = favoritesStore.clearAll()
}
