@file:Suppress("OPT_IN_USAGE")

package com.lol.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.SessionUseCase
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.LoginKey
import com.lol.app.navigation.ScreenKey
import com.lol.app.ui.screens.NavigationActions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MainViewModel.Factory::class)
class MainViewModel
@AssistedInject
constructor(
  private val sessionUseCase: SessionUseCase,
  @Assisted private val backStack: BackStack<ScreenKey>,
) : ViewModel(), NavigationActions {

  @AssistedFactory
  interface Factory {
    fun create(backStack: BackStack<ScreenKey>): MainViewModel
  }

  init {
    if (backStack.history.contains(InitialScreenKey)) {
      viewModelScope.launch {
        val emailAddress = sessionUseCase.getEmailAddress()
        backStack.setHistory(
          if (emailAddress == null) {
            LoginKey
          } else {
            ChampionListKey
          }
        )
      }
    }
  }

  override fun onLoginClicked(emailAddress: String) {
    viewModelScope.launch {
      sessionUseCase.updateEmailAddress(emailAddress)
      backStack.setHistory(ChampionListKey)
    }
  }

  override fun onLogoutClicked() {
    viewModelScope.launch {
      sessionUseCase.clear()
      backStack.setHistory(LoginKey)
    }
  }
}
