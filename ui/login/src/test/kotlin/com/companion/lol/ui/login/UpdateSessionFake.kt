package com.companion.lol.ui.login

import com.companion.lol.domain.usecase.UpdateSession

class UpdateSessionFake : UpdateSession {
  var callCount = 0

  override suspend fun invoke(emailAddress: String, autoSync: Boolean) {
    callCount++
  }

  override suspend fun invoke(autoSync: Boolean) = Unit
}
