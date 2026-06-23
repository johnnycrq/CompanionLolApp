package com.companion.lol.ui.settings

import com.companion.lol.domain.usecase.DeleteSession

class DeleteSessionFake : DeleteSession {
  var callCount = 0

  override suspend fun invoke() {
    callCount++
  }
}
