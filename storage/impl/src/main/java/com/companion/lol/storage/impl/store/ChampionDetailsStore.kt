package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.dbDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsQueries
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionDetailsStore @Inject constructor(database: LolAppDb) :
  SqldelightStore<ChampionDetailsQueries>(database.championDetailsQueries) {
  fun insert(details: ChampionDetailsTable) = queries.insert(details)

  fun observeByID(championId: ChampionId): Flow<ChampionDetailsTable?> =
    queries.findById(championId).asFlow().mapToOneOrNull(dbDispatcher)
}
