package com.companion.lol.ui.champion

import com.companion.lol.domain.model.UserSettings
import com.companion.lol.domain.usecase.impl.ObserveSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class ObserveSettingsFake : ObserveSettings {
  private val flow = MutableSharedFlow<UserSettings>(replay = 1)

  init {
    flow.tryEmit(UserSettings.DEFAULT)
  }

  override fun invoke(): Flow<UserSettings> = flow

  suspend fun emit(settings: UserSettings) {
    flow.emit(settings)
  }
}
