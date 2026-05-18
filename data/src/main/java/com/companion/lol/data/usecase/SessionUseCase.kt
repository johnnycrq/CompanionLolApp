package com.companion.lol.data.usecase

import com.companion.lol.storage.impl.model.ids.SessionId
import com.companion.lol.storage.impl.store.SessionStore
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionUseCase @Inject constructor(
    private val store: SessionStore
) {
    suspend fun getEmailAddress(): String? = withDbContext{
        store.get()?.emailAddress
    }

    suspend fun updateEmailAddress(emailAddress: String) = withDbContext{
        store.insert(
            details = SessionTable(id = SessionId, emailAddress = emailAddress)
        )
    }

    suspend fun clear() = withDbContext {
        store.delete().await()
    }
}