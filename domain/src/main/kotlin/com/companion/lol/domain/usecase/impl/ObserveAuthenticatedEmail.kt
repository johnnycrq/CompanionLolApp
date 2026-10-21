package com.companion.lol.domain.usecase.impl

import com.companion.lol.domain.usecase.ObserveAuthenticatedEmail
import com.companion.lol.storage.impl.store.SessionStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAuthenticatedEmailImpl @Inject constructor(private val sessionStore: SessionStore) :
  ObserveAuthenticatedEmail {
  override operator fun invoke(): Flow<String?> = sessionStore.observeEmailAddress()
}
