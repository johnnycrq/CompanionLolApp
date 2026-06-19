package com.companion.lol.domain.mapper

import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.model.ChampionTag
import com.companion.lol.domain.model.ChampionDetails
import com.companion.lol.domain.model.ChampionSkin
import com.companion.lol.domain.model.DdragonImage
import com.companion.lol.network.dto.ChampionDetailsDto
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import com.companion.lol.storage.sqldelight.tables.SkinTable

internal fun ChampionDetailsTable.model(keyName: String, skins: List<SkinTable>) =
  ChampionDetails(
    lore = this.lore,
    blurb = this.blurb,
    tags = this.tags,
    skins =
      skins.map {
        ChampionSkin(
          skinId = it.skinId,
          name = it.name,
          image = DdragonImage.Skin(keyName = keyName, skinNumber = it.number, skinName = it.name),
          isChroma = it.isChroma,
        )
      },
  )

internal fun ChampionDetailsDto.table(championId: ChampionId): ChampionDetailsTable {
  return this.info.let {
    ChampionDetailsTable(
      id = championId,
      lore = it.lore,
      blurb = it.blurb,
      tags = it.tags.map { ChampionTag.from(it) },
    )
  }
}
