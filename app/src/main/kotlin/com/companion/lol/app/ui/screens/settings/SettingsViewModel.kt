package com.companion.lol.app.ui.screens.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.app.io.worker.SyncWorker
import com.companion.lol.domain.model.UserSession
import com.companion.lol.domain.usecase.DeleteSession
import com.companion.lol.domain.usecase.ObserveSession
import com.companion.lol.domain.usecase.UpdateSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
  observeSession: ObserveSession,
  private val updateSession: UpdateSession,
  private val deleteSession: DeleteSession,
  private val application: Application,
) : ViewModel() {

  val state: StateFlow<SettingsState?> =
    observeSession()
      .filterIsInstance<UserSession.Authenticated>()
      .map { SettingsState(emailAddress = it.emailAddress, autoSync = it.autoSync) }
      .stateIn(viewModelScope, SharingStarted.Eagerly, null)

  fun onAutoSyncChanged(enabled: Boolean) {
    viewModelScope.launch {
      updateSession.invoke(enabled)

      if (enabled) {
        SyncWorker.schedulePeriodicSync(context = application)
      } else (SyncWorker.cancelPeriodicSync(application))
    }
  }

  fun onLogoutClicked() {
    viewModelScope.launch {
      deleteSession()
      SyncWorker.cancelPeriodicSync(application)
    }
  }
}
