package com.companion.lol.domain.usecase

import com.companion.lol.core.model.ChampionId

class UpdateFavoritesFake : UpdateFavorites {
  var callCount = 0
  var lastChampionId: ChampionId? = null
  var lastIsFavorite: Boolean? = null

  override suspend fun invoke(championId: ChampionId, isFavorite: Boolean) {
    callCount++
    lastChampionId = championId
    lastIsFavorite = isFavorite
  }
}
