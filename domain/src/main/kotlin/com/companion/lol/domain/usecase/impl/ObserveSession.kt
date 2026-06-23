package com.companion.lol.domain.usecase.impl

import com.companion.lol.domain.mapper.model
import com.companion.lol.domain.model.UserSession
import com.companion.lol.domain.usecase.ObserveSession
import com.companion.lol.storage.impl.store.SessionStore
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveSessionImpl @Inject constructor(private val sessionStore: SessionStore) :
  ObserveSession {
  override operator fun invoke(): Flow<UserSession> =
    sessionStore.observe().map(SessionTable?::model)
}
