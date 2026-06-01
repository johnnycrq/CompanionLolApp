package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.companion.lol.storage.impl.model.ids.SingleId
import com.companion.lol.storage.impl.model.other.GridSize
import com.companion.lol.storage.impl.model.other.SortOrder
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.impl.util.RequiresDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SettingsQueries
import com.companion.lol.storage.sqldelight.tables.SettingsTable
import com.companion.lol.storage.sqldelight.tables.SingletonSettingsTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private val default =
  SettingsTable(championGridSize = GridSize.MEDIUM, championSortOrder = SortOrder.ASC)

@Singleton
class SettingsStore @Inject constructor(database: LolAppDb, private val context: DatabaseContext) :
  SqldelightStore<SettingsQueries>(database.settingsQueries) {
  @RequiresDispatcher private fun findAdd(): SettingsTable? = queries.findAll().executeAsOneOrNull()
  fun observeOrDefault(): Flow<SettingsTable> =
    queries.findAll().asFlow().mapToOneOrDefault(default, context)

  suspend fun insert(championGridSize: GridSize? = null, championSortOrder: SortOrder? = null) =
    withContext(context) {
      queries.transaction {
        val current = findAdd() ?: default
        queries.insert(
          SingletonSettingsTable(
            id = SingleId,
            championGridSize = championGridSize ?: current.championGridSize,
            championSortOrder = championSortOrder ?: current.championSortOrder,
          )
        )
      }
    }
}
