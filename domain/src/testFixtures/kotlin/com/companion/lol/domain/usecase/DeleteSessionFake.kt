package com.companion.lol.domain.usecase

class DeleteSessionFake : DeleteSession {
  var callCount = 0

  override suspend fun invoke() {
    callCount++
  }
}
