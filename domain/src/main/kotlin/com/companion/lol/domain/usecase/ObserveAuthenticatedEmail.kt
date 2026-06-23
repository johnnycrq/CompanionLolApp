package com.companion.lol.domain.usecase

import com.companion.lol.domain.usecase.impl.ObserveAuthenticatedEmail
import com.companion.lol.storage.impl.store.SessionStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAuthenticatedEmailImpl @Inject constructor(private val sessionStore: SessionStore) :
  ObserveAuthenticatedEmail {
  override operator fun invoke(): Flow<String?> = sessionStore.observeEmailAddress()
}
