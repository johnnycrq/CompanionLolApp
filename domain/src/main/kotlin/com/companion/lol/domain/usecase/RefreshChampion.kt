package com.companion.lol.domain.usecase

import com.companion.lol.domain.mapper.table
import com.companion.lol.domain.util.getOrPropagate
import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.PartyTypeStore
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.impl.util.DatabaseTransacter
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

class RefreshChampion
@Inject
constructor(
  private val championStore: ChampionStore,
  private val partyTypeStore: PartyTypeStore,
  private val api: DDragonApi,
  private val databaseTransacter: DatabaseTransacter,
  private val databaseContext: DatabaseContext,
) {
  suspend operator fun invoke(forceRefresh: Boolean): Boolean {
    if (forceRefresh || !championStore.hasData()) {
      val champions =
        api.getChampionList().getOrPropagate {
          Timber.e(it)
          return false
        }

      withContext(databaseContext) {
        databaseTransacter.transaction {
          val data = champions.table()
          val partyType = data.table()
          championStore.insertAll(data)
          partyTypeStore.insertAll((partyType))
        }
      }
    }
    return true
  }
}
