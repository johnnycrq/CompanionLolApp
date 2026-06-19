package com.companion.lol.domain.usecase

import com.companion.lol.domain.mapper.model
import com.companion.lol.domain.model.UserSession
import com.companion.lol.storage.impl.store.SessionStore
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveSession @Inject constructor(private val sessionStore: SessionStore) {
  operator fun invoke(): Flow<UserSession> = sessionStore.observe().map(SessionTable?::model)
}
