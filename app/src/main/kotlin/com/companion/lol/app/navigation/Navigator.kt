package com.companion.lol.app.navigation

import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.ui.navigation.BackStack
import com.companion.lol.core.ui.navigation.Navigator
import com.companion.lol.ui.details.ChampionDetailsKey
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class NavigatorImpl @Inject constructor(private val backStack: BackStack) : Navigator {
  override fun navigateToChampionDetails(championId: ChampionId) {
    backStack.goTo(ChampionDetailsKey(championId))
  }
}
