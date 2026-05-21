package com.companion.lol.data.usecase

import com.companion.lol.storage.impl.model.ids.SessionId
import com.companion.lol.storage.impl.store.SessionStore
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class SessionUseCase @Inject constructor(private val store: SessionStore) {

  // we could observe the email address directly from the db
  // but for the purpose of this simple login logic lets assume
  // we will need something more complex in the future
  fun observeEmailAddress(): Flow<String?> = store.observe().map { it?.emailAddress }

  suspend fun updateEmailAddress(emailAddress: String) = withDbContext {
    store.insert(details = SessionTable(id = SessionId, emailAddress = emailAddress))
  }

  suspend fun clear() = withDbContext { store.delete().await() }
}
