package com.companion.lol.domain.usecase

import com.companion.lol.storage.impl.store.SessionStore
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject

class UpdateSession @Inject constructor(private val sessionStore: SessionStore) {
  suspend operator fun invoke(emailAddress: String, autoSync: Boolean) {
    sessionStore.insert(value = SessionTable(emailAddress = emailAddress, autoSync = autoSync))
  }

  suspend operator fun invoke(autoSync: Boolean) {
    sessionStore.updateAutoSync(autoSync = autoSync)
  }
}
