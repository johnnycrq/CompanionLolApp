package com.companion.lol.storage.impl.util

import com.companion.lol.storage.impl.model.ids.SingleId
import com.companion.lol.storage.sqldelight.tables.SessionTable
import com.companion.lol.storage.sqldelight.tables.SettingsTable
import com.companion.lol.storage.sqldelight.tables.SingletonSessionTable
import com.companion.lol.storage.sqldelight.tables.SingletonSettingsTable

fun SessionTable.toSingleton(): SingletonSessionTable {
  return SingletonSessionTable(
    id = SingleId,
    emailAddress = this.emailAddress,
    autoSync = this.autoSync,
  )
}

fun SettingsTable.toSingleton(): SingletonSettingsTable {
  return SingletonSettingsTable(
    id = SingleId,
    championGridSize = this.championGridSize,
    championSortOrder = this.championSortOrder,
  )
}
