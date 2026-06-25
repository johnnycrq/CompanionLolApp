package com.companion.lol.domain.usecase

import com.companion.lol.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class ObserveSessionFake : ObserveSession {
  private val flow = MutableSharedFlow<UserSession>(replay = 1)

  override fun invoke(): Flow<UserSession> = flow

  suspend fun emit(session: UserSession) {
    flow.emit(session)
  }
}
