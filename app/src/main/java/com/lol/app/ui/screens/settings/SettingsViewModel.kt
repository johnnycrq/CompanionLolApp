package com.lol.app.ui.screens.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.SessionUseCase
import com.lol.app.SyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel
@Inject
constructor(private val sessionUseCase: SessionUseCase, private val application: Application) :
  ViewModel() {

  val state: StateFlow<SettingsState?> =
    sessionUseCase
      .observe()
      .filterNotNull()
      .map { SettingsState(emailAddress = it.emailAddress, autoSync = it.autoSync) }
      .stateIn(viewModelScope, SharingStarted.Eagerly, null)

  fun onAutoSyncChanged(enabled: Boolean) {
    viewModelScope.launch {
      sessionUseCase.updateAutoSync(enabled)

      if (enabled) {
        SyncWorker.schedulePeriodicSync(context = application)
      } else (SyncWorker.cancelPeriodicSync(application))
    }
  }

  fun onLogoutClicked() {
    viewModelScope.launch {
      sessionUseCase.clear()
      SyncWorker.cancelPeriodicSync(application)
    }
  }
}
