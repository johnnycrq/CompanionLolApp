package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.dbDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionQueries
import com.companion.lol.storage.sqldelight.tables.ChampionTable
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ChampionStore @Inject constructor(database: LolAppDb) :
  SqldelightStore<ChampionQueries>(database.championQueries) {

  fun insertAll(champions: List<ChampionTable>) {
    queries.transaction { champions.forEach { queries.insert(it) } }
  }

  fun hasData(): Boolean = queries.hasData().executeAsOne()

  fun observeAllWithFavorites(): Flow<List<ChampionWithFavoritesView>> =
    queries.findAll().asFlow().mapToList(dbDispatcher)

  fun observeWithFavoritesById(championId: ChampionId): Flow<ChampionWithFavoritesView> =
    queries.findById(championId).asFlow().mapToOne(dbDispatcher)

  fun findWithFavoritesById(championId: ChampionId): ChampionWithFavoritesView? =
    queries.findById(championId).executeAsOneOrNull()

  fun findKeyNameById(championId: ChampionId): String? =
    queries.findKeyNameById(championId).executeAsOneOrNull()
}
