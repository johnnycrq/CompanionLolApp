package com.companion.lol.domain.usecase

import com.companion.lol.storage.impl.store.SessionStore
import javax.inject.Inject

class DeleteSession @Inject constructor(private val sessionStore: SessionStore) {
  suspend operator fun invoke() = sessionStore.delete()
}
