package com.companion.lol.data.usecase

import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.ids.SkinId
import com.companion.lol.storage.impl.model.other.ChampionTag
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SkinStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.impl.util.DbTransactor
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import com.companion.lol.storage.sqldelight.tables.SkinTable
import javax.inject.Inject
import kotlinx.coroutines.withContext

class RefreshChampionDetailsUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val championDetailsStore: ChampionDetailsStore,
  private val skinsStore: SkinStore,
  private val dDragonApi: DDragonApi,
  private val transacter: DbTransactor,
  private val dispatcher: DbDispatcher,
) {
  suspend fun refresh(championId: ChampionId): Result<Unit> {
    val championKeyName = championStore.findKeyNameById(championId)

    val champion =
      dDragonApi
        .getChampionDetails(championName = championKeyName)
        .getOrElse {
          return Result.failure(it)
        }
        .info

    withContext(dispatcher) {
      transacter.transaction {
        val skins =
          champion.skins
            // parentSkin are chroma skins, don't map directly to url
            .filter { it.parentSkin == null }
            .map {
              SkinTable(
                id = championId,
                skinId = SkinId(it.id),
                number = it.num,
                name = it.name,
                isChroma = it.chromas,
              )
            }

        championDetailsStore.insert(
          ChampionDetailsTable(
            id = championId,
            lore = champion.lore,
            blurb = champion.blurb,
            tags = champion.tags.map { ChampionTag.from(it) },
            partyTypeId = PartyType.from(champion.partytype).dbId,
          )
        )
        skinsStore.insertAll(skins)
      }
    }
    return Result.success(Unit)
  }
}
