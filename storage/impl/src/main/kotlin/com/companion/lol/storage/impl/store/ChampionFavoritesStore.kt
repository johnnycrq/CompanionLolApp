package com.companion.lol.storage.impl.store

import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionFavoritesQueries
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.withContext

@Singleton
class ChampionFavoritesStore
@Inject
constructor(database: LolAppDb, private val context: DatabaseContext) :
  SqldelightStore<ChampionFavoritesQueries>(database.championFavoritesQueries) {
  suspend fun markFavorite(championId: ChampionId, isFavorite: Boolean) =
    withContext(context) { queries.updateIsFavorite(championId, isFavorite) }

  suspend fun clearAll() = withContext(context) { queries.clearAll() }
}
