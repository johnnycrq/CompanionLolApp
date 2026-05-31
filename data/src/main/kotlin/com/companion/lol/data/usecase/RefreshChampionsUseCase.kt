package com.companion.lol.data.usecase

import com.companion.lol.data.mapper.toTable
import com.companion.lol.data.other.CompletableResult
import com.companion.lol.data.util.getOrPropagate
import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.PartyTypeStore
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.impl.util.DatabaseTransacter
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.withContext
import timber.log.Timber

@Singleton
class RefreshChampionsUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val partyTypeStore: PartyTypeStore,
  private val api: DDragonApi,
  private val databaseTransacter: DatabaseTransacter,
  private val databaseContext: DatabaseContext,
) {
  suspend fun refresh(): CompletableResult {
    val champions =
      api.getChampionList().getOrPropagate {
        Timber.e(it)
        return Result.failure(it)
      }

    withContext(databaseContext) {
      databaseTransacter.transaction {
        val data = champions.toTable()
        val partyType = data.toTable()
        championStore.insertAll(data)
        partyTypeStore.insertAll((partyType))
      }
    }
    return Result.success(Unit)
  }
}
