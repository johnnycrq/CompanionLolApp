package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.impl.util.toSingleton
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SessionQueries
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Singleton
class SessionStore @Inject constructor(database: LolAppDb, private val context: DatabaseContext) :
  SqldelightStore<SessionQueries>(database.sessionQueries) {

  suspend fun insert(value: SessionTable): Unit =
    withContext(context) { queries.insert(value.toSingleton()).await() }

  fun observe(): Flow<SessionTable?> = queries.get().asFlow().mapToOneOrNull(context)

  fun observeEmailAddress(): Flow<String?> = queries.emailAddress().asFlow().mapToOneOrNull(context)

  suspend fun updateAutoSync(autoSync: Boolean): Unit =
    withContext(context) { queries.updateAutoSync(autoSync).await() }

  suspend fun delete(): Unit = withContext(context) { queries.delete().await() }
}
