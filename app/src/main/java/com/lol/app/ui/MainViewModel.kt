@file:Suppress("OPT_IN_USAGE")

package com.lol.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.SessionStore
import com.lol.app.compose.ui.theme.Gold1
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.BackStack.Companion.backStack
import com.lol.app.navigation.keys.ChampionDetailsKey
import com.lol.app.navigation.keys.ChampionListKey
import com.lol.app.navigation.keys.InitialScreenKey
import com.lol.app.navigation.keys.LoginKey
import com.lol.app.navigation.keys.ScreenKey
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
constructor(private val sessionStore: SessionStore, savedStateHandle: SavedStateHandle) :
  ViewModel(), AppActions {
  val backStack: BackStack<ScreenKey> =
    savedStateHandle.backStack(initialHistory = listOf(InitialScreenKey))
  val colorCache: ChampionColorCache =
    ChampionColorCache.Impl(scope = viewModelScope, defaultColor = Gold1)

  init {
    viewModelScope.launch {
      sessionStore
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
                .dropLastWhile { it.requiresAuth() }
                .filterNot { it is InitialScreenKey }
                .ifEmpty { listOf(LoginKey) }
            )
          } else {
            // if we are logged in and on a public-only stack, go to main content
            if (currentHistory.none { it.requiresAuth() }) {
              backStack.setHistory(ChampionListKey)
            }
          }
        }
    }
  }

  override fun goToChampionDetails(championId: ChampionId) {
    backStack.goTo(ChampionDetailsKey(championId))
  }
}
