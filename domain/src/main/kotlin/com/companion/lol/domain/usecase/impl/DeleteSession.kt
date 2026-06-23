package com.companion.lol.domain.usecase.impl

import com.companion.lol.domain.usecase.DeleteSession
import com.companion.lol.storage.impl.store.SessionStore
import javax.inject.Inject

class DeleteSessionImpl @Inject constructor(private val sessionStore: SessionStore) :
  DeleteSession {
  override suspend operator fun invoke() = sessionStore.delete()
}
