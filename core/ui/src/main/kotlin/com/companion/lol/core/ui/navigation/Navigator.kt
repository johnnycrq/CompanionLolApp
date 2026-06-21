package com.companion.lol.core.ui.navigation

import com.companion.lol.core.model.ChampionId

interface Navigator {
  fun navigateToChampionDetails(championId: ChampionId)
}
