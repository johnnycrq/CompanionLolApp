package com.companion.lol.domain.mapper

import com.companion.lol.core.model.PartyType
import com.companion.lol.storage.impl.model.ids.PartyTypeId
import com.companion.lol.storage.sqldelight.tables.ChampionPartyTypeTable
import com.companion.lol.storage.sqldelight.tables.ChampionTable

internal fun List<ChampionTable>.table(): List<ChampionPartyTypeTable> =
  this.distinctBy { item -> item.partTypeId }
    .map { item -> item.partTypeId.toPartyType() }
    .map { partyType -> ChampionPartyTypeTable(id = partyType.dbId, partyType) }

private fun PartyTypeId.toPartyType(): PartyType {
  return PartyType.entries.first { it.ordinal == this.value }
}

internal val PartyType.dbId: PartyTypeId
  get() = PartyTypeId(this.ordinal)
