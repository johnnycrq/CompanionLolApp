package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.companion.lol.storage.impl.model.ids.SettingsId
import com.companion.lol.storage.impl.model.other.SortOrder
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SettingsQueries
import com.companion.lol.storage.sqldelight.tables.SettingsTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

private val default = SettingsTable(SettingsId, 4, SortOrder.ASC)

@Singleton
class SettingsStore @Inject constructor(database: LolAppDb) :
  SqldelightStore<SettingsQueries>(database.settingsQueries) {
  private fun find(): SettingsTable = queries.findAll().executeAsOneOrNull() ?: default

  fun observe(dispatcher: CoroutineDispatcher): Flow<SettingsTable> =
    queries.findAll().asFlow().mapToOneOrDefault(default, dispatcher)

  fun insert(championRotationGridSize: Int? = null, championRotationSortOrder: SortOrder? = null) {
    queries.transaction {
      val current = find()
      queries.insert(
        SettingsTable(
          id = SettingsId,
          championRotationGridSize = championRotationGridSize ?: current.championRotationGridSize,
          championRotationSortOrder = championRotationSortOrder ?: current.championRotationSortOrder,
        )
      )
    }
  }
}
