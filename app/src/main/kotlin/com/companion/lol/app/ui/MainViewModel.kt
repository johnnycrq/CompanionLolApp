@file:Suppress("OPT_IN_USAGE")

package com.companion.lol.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.app.navigation.BackStackImpl
import com.companion.lol.app.util.ChampionColorCache
import com.companion.lol.core.io.AppDispatchers
import com.companion.lol.core.ui.navigation.EntryScreenKey
import com.companion.lol.core.ui.navigation.ScreenKeySerializerModule
import com.companion.lol.core.ui.theme.Gold1
import com.companion.lol.domain.usecase.impl.ObserveAuthenticatedEmail
import com.companion.lol.ui.champion.ChampionKey
import com.companion.lol.ui.login.LoginKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(
  savedStateHandle: SavedStateHandle,
  dispatchers: AppDispatchers,
  screenKeySerializerModule: ScreenKeySerializerModule,
  val backStack: BackStackImpl,
  val snackBarManager: SnackBarManager,
  private val observeLoggedInEmailAddress: ObserveAuthenticatedEmail,
) : ViewModel() {
  /**
   * we need this to survive rotation but not process death because the images will be refetched and
   * color can change. Using rememberSaveable in the UI layer would need to save the whole list of
   * colors, using retain would work but the scope would break on rotation. ViewModel scope usage
   * here is the best option imo
   */
  val colorCache =
    viewModelScope.ChampionColorCache(
      extractContext = dispatchers.computation,
      defaultColor = Gold1,
    )

  init {
    backStack.attachSaver(
      savedStateHandle = savedStateHandle,
      serializerModule = screenKeySerializerModule,
      restore = true,
    )

    viewModelScope.launch {
      observeLoggedInEmailAddress()
        .map { it != null }
        .distinctUntilChanged()
        .collectLatest { isLoggedIn ->
          // at this point the backstack could have been
          // potentially restored from saved state after process death
          val currentHistory = backStack.history
          if (!isLoggedIn) {
            // if we are logged out we rewrite the history.
            // we drop private screens and ensure we don't stay on Initial
            backStack.setHistory(
              currentHistory
                .dropLastWhile { it.requiresAuth() }
                .filterNot { it is EntryScreenKey }
                .ifEmpty { listOf(LoginKey) }
            )
          } else {
            // if we are logged in and on a public-only stack, go to main content
            if (currentHistory.none { it.requiresAuth() }) {
              backStack.setHistory(ChampionKey)
            }
          }
        }
    }
  }
}
