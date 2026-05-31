package com.companion.lol.data.usecase

import com.companion.lol.data.mapper.toTable
import com.companion.lol.data.other.CompletableResult
import com.companion.lol.data.util.getOrPropagate
import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SkinStore
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.impl.util.DatabaseTransacter
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

class RefreshChampionDetailsUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val championDetailsStore: ChampionDetailsStore,
  private val skinsStore: SkinStore,
  private val dDragonApi: DDragonApi,
  private val databaseTransacter: DatabaseTransacter,
  private val databaseContext: DatabaseContext,
) {
  suspend fun refresh(championId: ChampionId): CompletableResult {
    val championKeyName = withContext(databaseContext) { championStore.findKeyNameById(championId) }

    val champion =
      dDragonApi.getChampionDetails(championName = championKeyName).getOrPropagate {
        Timber.e(it)
        return Result.failure(it)
      }

    withContext(databaseContext) {
      databaseTransacter.transaction {
        championDetailsStore.insert(champion.toTable(championId))
        skinsStore.insertAll(champion.info.skins.toTable(championId))
      }
    }

    return Result.success(Unit)
  }
}
