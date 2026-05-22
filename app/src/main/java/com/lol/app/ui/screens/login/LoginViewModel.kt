package com.lol.app.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.SessionUseCase
import com.companion.lol.storage.impl.model.ids.SessionId
import com.companion.lol.storage.sqldelight.tables.SessionTable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val sessionUseCase: SessionUseCase) : ViewModel() {
  val state: StateFlow<LoginState>
    field = MutableStateFlow(LoginState())

  fun onEmailChanged(newEmail: String) {
    state.update { it.copy(email = newEmail) }
  }

  fun onLoginClicked() {
    val currentState = this.state.value
    // fail-safe
    if (!currentState.isEmailValid) return

    viewModelScope.launch {
      sessionUseCase.insert(
        data = SessionTable(id = SessionId, emailAddress = currentState.email, autoSync = false)
      )
    }
  }
}
