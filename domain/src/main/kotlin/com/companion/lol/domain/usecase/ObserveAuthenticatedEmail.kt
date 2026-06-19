package com.companion.lol.domain.usecase

import com.companion.lol.storage.impl.store.SessionStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAuthenticatedEmail @Inject constructor(private val sessionStore: SessionStore) {
  operator fun invoke(): Flow<String?> = sessionStore.observeEmailAddress()
}
