package com.companion.lol.domain.usecase.impl

import com.companion.lol.domain.usecase.UpdateSession
import com.companion.lol.storage.impl.store.SessionStore
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject

class UpdateSessionImpl @Inject constructor(private val sessionStore: SessionStore) :
  UpdateSession {
  override suspend operator fun invoke(emailAddress: String, autoSync: Boolean) {
    sessionStore.insert(value = SessionTable(emailAddress = emailAddress, autoSync = autoSync))
  }

  override suspend operator fun invoke(autoSync: Boolean) {
    sessionStore.updateAutoSync(autoSync = autoSync)
  }
}
