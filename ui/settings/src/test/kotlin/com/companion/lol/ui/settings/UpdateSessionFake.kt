package com.companion.lol.ui.settings

import com.companion.lol.domain.usecase.UpdateSession

class UpdateSessionFake : UpdateSession {
  var callCount = 0
  var lastEmailAddress: String? = null
  var lastAutoSync: Boolean? = null

  override suspend fun invoke(emailAddress: String, autoSync: Boolean) {
    callCount++
    lastEmailAddress = emailAddress
    lastAutoSync = autoSync
  }

  override suspend fun invoke(autoSync: Boolean) {
    callCount++
    lastAutoSync = autoSync
  }
}
