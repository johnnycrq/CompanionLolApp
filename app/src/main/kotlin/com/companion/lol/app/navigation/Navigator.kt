package com.companion.lol.app.navigation

import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.ui.navigation.BackStack
import com.companion.lol.core.ui.navigation.Navigator
import com.companion.lol.ui.details.ChampionDetailsKey

class NavigatorImpl(private val backStack: BackStack) : Navigator {
  override fun navigateToChampionDetails(championId: ChampionId) {
    backStack.goTo(ChampionDetailsKey(championId))
  }
}
