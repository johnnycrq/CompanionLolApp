package com.companion.lol.domain.usecase

import com.companion.lol.domain.model.UserSession
import kotlinx.coroutines.flow.Flow

interface ObserveSession {
  operator fun invoke(): Flow<UserSession>
}
