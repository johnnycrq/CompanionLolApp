package com.companion.lol.data.mapper

import com.companion.lol.data.io.images.DdragonImage
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.data.util.capitalizeWords
import com.companion.lol.network.dto.ChampionListDto
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.sqldelight.tables.ChampionPartyTypeTable
import com.companion.lol.storage.sqldelight.tables.ChampionTable
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView

fun ChampionWithFavoritesView.toModel() =
  ChampionModel(
    id = this.id,
    name = this.name,
    keyName = this.keyName,
    title = this.title,
    squareImage = DdragonImage.Square(this.squareImageName),
    partyType = this.partyType,
    isFavorite = this.isFavorite ?: false,
  )

fun ChampionListDto.toTable(): List<ChampionTable> {
  return this.champions.map {
    ChampionTable(
      id = ChampionId(it.championKey),
      keyName = it.id,
      name = it.name,
      title = it.title.capitalizeWords(),
      squareImageName = it.image.full,
      partTypeId = PartyType.from(it.partType).dbId,
    )
  }
}

fun List<ChampionTable>.toTable() =
  this.distinctBy { item -> item.partTypeId }
    .map { item -> PartyType.from(item.partTypeId) }
    .map { partyType -> ChampionPartyTypeTable(id = partyType.dbId, partyType) }
