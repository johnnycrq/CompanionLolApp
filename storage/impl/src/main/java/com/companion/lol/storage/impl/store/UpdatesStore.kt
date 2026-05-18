package com.companion.lol.storage.impl.store

import com.companion.lol.storage.impl.model.ids.SettingsId
import com.companion.lol.storage.impl.model.model.OffsetDateTime
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.UpdatesQueries
import com.companion.lol.storage.sqldelight.tables.UpdatesTable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdatesStore
@Inject
constructor(database: LolAppDb) :
  SqldelightStore<UpdatesQueries>(database.updatesQueries) {
  fun find(): UpdatesTable? = queries.findAll().executeAsOneOrNull()

  fun insert(champion: OffsetDateTime? = null, rotation: OffsetDateTime? = null) {
    queries.transaction {
      val current = find()
      queries.insert(
        UpdatesTable(
          id = SettingsId,
          champion = champion ?: current?.champion,
          rotation = rotation ?: current?.rotation,
        )
      )
    }
  }
}
