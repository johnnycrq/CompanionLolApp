package com.companion.lol.storage.impl.store

import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.AppDispatchers
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionFavoritesQueries
import com.companion.lol.storage.sqldelight.tables.ChampionFavoritesTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.withContext

@Singleton
class ChampionFavoritesStore
@Inject
constructor(database: LolAppDb, private val dispatchers: AppDispatchers) :
  SqldelightStore<ChampionFavoritesQueries>(database.championFavoritesQueries) {
  suspend fun markFavorite(championId: ChampionId, isFavorite: Boolean) =
    withContext(dispatchers.io) {
      queries.insert(ChampionFavoritesTable(championId, isFavorite)).await()
    }

  suspend fun clearAll() = withContext(dispatchers.io) { queries.clearAll().await() }
}
