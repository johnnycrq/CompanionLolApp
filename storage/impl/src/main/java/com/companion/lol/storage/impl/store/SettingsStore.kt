package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.companion.lol.storage.impl.model.ids.SettingsId
import com.companion.lol.storage.impl.model.other.SortOrder
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SettingsQueries
import com.companion.lol.storage.sqldelight.tables.SettingsTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

private val default = SettingsTable(SettingsId, 4, SortOrder.ASC)

@Singleton
class SettingsStore @Inject constructor(database: LolAppDb, private val dispatcher: DbDispatcher) :
  SqldelightStore<SettingsQueries>(database.settingsQueries) {
  private fun find(): SettingsTable = queries.findAll().executeAsOneOrNull() ?: default

  fun observe(): Flow<SettingsTable> =
    queries.findAll().asFlow().mapToOneOrDefault(default, dispatcher)

  suspend fun insert(
    championRotationGridSize: Int? = null,
    championRotationSortOrder: SortOrder? = null,
  ) =
    withContext(dispatcher) {
      queries.transaction {
        val current = find()
        queries.insert(
          SettingsTable(
            id = SettingsId,
            championRotationGridSize = championRotationGridSize ?: current.championRotationGridSize,
            championRotationSortOrder =
              championRotationSortOrder ?: current.championRotationSortOrder,
          )
        )
      }
    }
}
