package com.companion.lol.domain.mapper

import com.companion.lol.domain.model.UserSession
import com.companion.lol.storage.sqldelight.tables.SessionTable

internal fun SessionTable?.model(): UserSession {
  return if (this == null) {
    UserSession.LoggedOut
  } else {
    UserSession.Authenticated(this.emailAddress, this.autoSync)
  }
}
