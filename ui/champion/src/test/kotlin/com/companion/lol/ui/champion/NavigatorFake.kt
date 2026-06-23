package com.companion.lol.ui.champion

import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.ui.navigation.Navigator

class NavigatorFake : Navigator {
  var navigateCount = 0
  var lastChampionId: ChampionId? = null

  override fun navigateToChampionDetails(championId: ChampionId) {
    navigateCount++
    lastChampionId = championId
  }
}
