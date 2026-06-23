package com.companion.lol.domain.usecase

import com.companion.lol.core.model.ChampionId

interface UpdateFavorites {
  suspend operator fun invoke(championId: ChampionId, isFavorite: Boolean)
}
