package com.lol.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(private val sessionUseCase: SessionUseCase) :
  ViewModel() {

  fun onLogoutClicked() {
    viewModelScope.launch { sessionUseCase.clear() }
  }
}
