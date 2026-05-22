package com.companion.lol.data.usecase

import com.companion.lol.storage.impl.store.SessionStore
import com.companion.lol.storage.impl.util.CompanionLolTransactor
import com.companion.lol.storage.impl.util.dbDispatcher
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class SessionUseCase
@Inject
constructor(private val store: SessionStore, private val transactor: CompanionLolTransactor) {

  fun observeEmailAddress(): Flow<String?> = store.observeEmailAddress(dbDispatcher)

  suspend fun insert(data: SessionTable) = withDbContext { store.insert(data) }

  suspend fun updateAutoSync(autoSync: Boolean) = withDbContext {
    transactor.transaction { store.insert(store.get().copy(autoSync = autoSync)) }
  }

  fun observe(): Flow<SessionTable?> = store.observe(dbDispatcher)

  suspend fun clear() = withDbContext { store.delete().await() }
}
