package com.lol.app.ui.screens.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lol.app.base.persistedFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
  val state: StateFlow<LoginState>
  field = savedStateHandle.persistedFlow(LoginState())

  fun onEmailChanged(newEmail: String) {
    state.update { it.copy(email = newEmail) }
  }
}
