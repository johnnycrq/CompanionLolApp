package com.companion.lol.domain.usecase.impl

import com.companion.lol.core.model.ChampionId
import com.companion.lol.domain.mapper.table
import com.companion.lol.domain.usecase.RefreshChampionDetails
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

class RefreshChampionDetailsImpl
@Inject
constructor(
  private val championStore: ChampionStore,
  private val championDetailsStore: ChampionDetailsStore,
  private val skinsStore: SkinStore,
  private val dDragonApi: DDragonApi,
  private val databaseTransacter: DatabaseTransacter,
  private val databaseContext: DatabaseContext,
) : RefreshChampionDetails {
  override suspend operator fun invoke(
    retry: Int,
    retryDelay: Duration,
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
