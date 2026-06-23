package com.companion.lol.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.core.io.flow.persistedFlow
import com.companion.lol.domain.usecase.UpdateSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel
@Inject
constructor(private val updateSession: UpdateSession, savedStateHandle: SavedStateHandle) :
  ViewModel() {
  val state: StateFlow<LoginState>
    field = savedStateHandle.persistedFlow(key = "LoginState", LoginState())

  fun onEmailChanged(newEmail: String) {
    state.update { it.copy(email = newEmail) }
  }

  fun onLoginClicked() {
    val currentState = this.state.value
    // fail-safe
    if (!currentState.isEmailValid) return

    viewModelScope.launch { updateSession(emailAddress = currentState.email, autoSync = false) }
  }
}
