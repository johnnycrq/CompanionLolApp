package com.lol.app.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val sessionUseCase: SessionUseCase
) : ViewModel() {
  val state: StateFlow<LoginState>
    field = MutableStateFlow(LoginState())

  fun onEmailChanged(newEmail: String) {
    state.update { it.copy(email = newEmail) }
  }

  fun onLoginClicked() {
    val currentState = this.state.value
    // fail-safe
    if(!currentState.isEmailValid) return

    viewModelScope.launch {
      sessionUseCase.updateEmailAddress(currentState.email)
    }
  }
}
