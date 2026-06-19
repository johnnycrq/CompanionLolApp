package com.companion.lol.domain.usecase

import com.companion.lol.core.model.ChampionId
import com.companion.lol.domain.mapper.model
import com.companion.lol.domain.model.ChampionDetails
import com.companion.lol.domain.model.ChampionWithDetails
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SkinStore
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@Suppress("OPT_IN_USAGE")
class ObserveChampionDetails
@Inject
constructor(
  private val championStore: ChampionStore,
  private val skinsStore: SkinStore,
  private val championDetailsStore: ChampionDetailsStore,
) {
  operator fun invoke(championId: ChampionId): Flow<ChampionWithDetails> {
    return flow { emit(championStore.findKeyNameById(championId)) }
      .flatMapLatest { keyName ->
        combine(
          flow =
            championStore
              .observeWithFavoritesById(championId)
              .map(ChampionWithFavoritesView::model),
          flow2 = observeChampionDetails(championId, keyName),
          transform = ::ChampionWithDetails,
        )
      }
  }

  private fun observeChampionDetails(
    championId: ChampionId,
    keyName: String,
  ): Flow<ChampionDetails?> =
    combine(
      championDetailsStore.observeByID(championId),
      skinsStore.observeByChampionId(championId),
    ) { details, skins ->
      details?.model(keyName, skins)
    }
}
