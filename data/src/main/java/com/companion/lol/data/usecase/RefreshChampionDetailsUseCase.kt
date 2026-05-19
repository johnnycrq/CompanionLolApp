package com.companion.lol.data.usecase

import app.cash.sqldelight.Transacter
import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.ids.SkinId
import com.companion.lol.storage.impl.model.other.ChampionTag
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.SkinStore
import com.companion.lol.storage.impl.util.CompanionLolTransactor
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import com.companion.lol.storage.sqldelight.tables.SkinTable
import javax.inject.Inject

class RefreshChampionDetailsUseCase @Inject constructor(
    private val championDetailsStore: ChampionDetailsStore,
    private val skinsStore: SkinStore,
    private val dDragonApi: DDragonApi,
    private val transacter: CompanionLolTransactor
) {
    suspend fun refresh(championId: ChampionId, championKeyName: String) {
        val champion = dDragonApi.getChampionDetails(
            championName = championKeyName
        ).info

        withDbContext {
            transacter.transaction {
                val skins = champion.skins.map {
                    SkinTable(
                        id = championId,
                        skinId = SkinId(it.id),
                        number = it.num,
                        name = it.name,
                        isChroma = it.chromas
                    )
                }

                championDetailsStore.insert(
                    ChampionDetailsTable(
                        id = championId,
                        lore = champion.lore,
                        blurb = champion.blurb,
                        tags = champion.tags.map { ChampionTag.from(it) },
                        partyTypeId = PartyType.from(champion.partytype).dbId
                    )
                )
                skinsStore.insertAll(skins)
            }
        }
    }
}