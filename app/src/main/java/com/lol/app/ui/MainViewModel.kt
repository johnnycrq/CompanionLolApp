@file:Suppress("OPT_IN_USAGE")

package com.lol.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.SessionUseCase
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.base.theme.Gold1
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.BackStack.Companion.backStack
import com.lol.app.navigation.ChampionDetailsKey
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.LoginKey
import com.lol.app.navigation.ScreenKey
import com.lol.app.util.ChampionColorCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(private val sessionUseCase: SessionUseCase, savedStateHandle: SavedStateHandle) :
  ViewModel() {
  val backStack: BackStack<ScreenKey> =
    savedStateHandle.backStack(initialHistory = listOf(InitialScreenKey))
  val colorCache: ChampionColorCache =
    ChampionColorCache.Impl(scope = viewModelScope, defaultColor = Gold1)

  init {
    viewModelScope.launch {
      sessionUseCase
        .observeEmailAddress()
        .map { it != null }
        .distinctUntilChanged()
        .collectLatest { isLoggedIn ->
          // at this point the backstack could have been
          // potentially restored from saved state
          val currentHistory = backStack.history
          if (!isLoggedIn) {
            // if we are logged out we rewrite the history.
            // we drop private screens and ensure we don't stay on Initial (placeHolder)
            backStack.setHistory(
              currentHistory
                .dropLastWhile { it.requiresAuth }
                .filterNot { it is InitialScreenKey }
                .ifEmpty { listOf(LoginKey) }
            )
          } else {
            // if we are logged in and on a public-only stack, go to main content
            if (currentHistory.none { it.requiresAuth }) {
              backStack.setHistory(ChampionListKey)
            }
          }
        }
    }
  }

  fun goToChampionDetails(championId: ChampionId) {
    backStack.goTo(ChampionDetailsKey(championId))
  }
}
