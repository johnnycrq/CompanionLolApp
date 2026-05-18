package com.companion.lol.storage.impl.store

import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionFavoritesQueries
import com.companion.lol.storage.sqldelight.tables.ChampionFavoritesTable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionFavoritesStore @Inject constructor(database: LolAppDb) :
  SqldelightStore<ChampionFavoritesQueries>(database.championFavoritesQueries) {

  fun markFavorite(championId: ChampionId, isFavorite: Boolean) {
    queries.insert(ChampionFavoritesTable(championId, isFavorite))
  }

  fun clearAll() = queries.clearAll()
}
