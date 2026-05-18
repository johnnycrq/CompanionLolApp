package com.companion.lol.storage.impl.store

import com.companion.lol.storage.impl.model.ids.SessionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SessionQueries
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStore @Inject constructor(database: LolAppDb) :
  SqldelightStore<SessionQueries>(database.sessionQueries) {
  fun get(): SessionTable? = queries.get().executeAsOneOrNull()

  fun insert(details: SessionTable) {
    queries.insert(details.copy(id = SessionId))
  }

  fun delete() = queries.delete()
}
