package com.companion.lol.domain.usecase

import com.companion.lol.core.model.ChampionId
import com.companion.lol.domain.mapper.table
import com.companion.lol.domain.util.getOrPropagate
import com.companion.lol.domain.util.withRetry
import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SkinStore
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.impl.util.DatabaseTransacter
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.withContext
import timber.log.Timber

class RefreshChampionDetails
@Inject
constructor(
  private val championStore: ChampionStore,
  private val championDetailsStore: ChampionDetailsStore,
  private val skinsStore: SkinStore,
  private val dDragonApi: DDragonApi,
  private val databaseTransacter: DatabaseTransacter,
  private val databaseContext: DatabaseContext,
) {
  suspend operator fun invoke(
    retry: Int = 3,
    retryDelay: Duration = 1.seconds,
    championId: ChampionId,
  ): Boolean =
    withRetry(times = 1, delayDuration = 1.seconds) { refresh(championId) }.getOrNull() != null

  private suspend fun refresh(championId: ChampionId): Result<Unit> {
    val championKeyName = withContext(databaseContext) { championStore.findKeyNameById(championId) }

    val champion =
      dDragonApi.getChampionDetails(championName = championKeyName).getOrPropagate {
        Timber.e(it)
        return Result.failure(it)
      }

    withContext(databaseContext) {
      databaseTransacter.transaction {
        championDetailsStore.insert(champion.table(championId))
        skinsStore.insertAll(champion.info.skins.table(championId))
      }
    }

    return Result.success(Unit)
  }
}
