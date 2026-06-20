package com.companion.lol.domain.mapper

import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.model.DdragonImage
import com.companion.lol.core.model.PartyType
import com.companion.lol.domain.model.Champion
import com.companion.lol.domain.util.capitalizeWords
import com.companion.lol.network.EndPoints
import com.companion.lol.network.dto.ChampionListDto
import com.companion.lol.storage.sqldelight.tables.ChampionTable
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView

internal fun ChampionWithFavoritesView.model() =
  Champion(
    id = this.id,
    name = this.name,
    keyName = this.keyName,
    title = this.title,
    squareImage = DdragonImage.Square(EndPoints.DDragon.championSquareAsset(this.squareImageName)),
    partyType = this.partyType,
    isFavorite = this.isFavorite ?: false,
  )

internal fun ChampionListDto.table(): List<ChampionTable> {
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
