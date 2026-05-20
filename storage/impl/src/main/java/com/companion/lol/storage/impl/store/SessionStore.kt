package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.companion.lol.storage.impl.model.ids.SessionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.dbDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SessionQueries
import com.companion.lol.storage.sqldelight.tables.SessionTable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStore @Inject constructor(database: LolAppDb) :
  SqldelightStore<SessionQueries>(database.sessionQueries) {
  fun observe(): Flow<SessionTable?> = queries.get()
    .asFlow().mapToOneOrNull(dbDispatcher)

  fun insert(details: SessionTable) {
    queries.insert(details.copy(id = SessionId))
  }

  fun delete() = queries.delete()
}
