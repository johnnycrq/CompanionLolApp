package com.companion.lol.data.mapper

import com.companion.lol.data.io.images.DdragonImage
import com.companion.lol.data.model.ChampionDetailsModel
import com.companion.lol.data.model.other.ChampionSkin
import com.companion.lol.network.dto.ChampionDetailsDto
import com.companion.lol.network.dto.other.ChampionSkins
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.ids.SkinId
import com.companion.lol.storage.impl.model.other.ChampionTag
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import com.companion.lol.storage.sqldelight.tables.SkinTable

fun ChampionDetailsTable.toModel(keyName: String, skins: List<SkinTable>) =
  ChampionDetailsModel(
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

fun ChampionDetailsDto.toTable(championId: ChampionId): ChampionDetailsTable {
  return this.info.let {
    ChampionDetailsTable(
      id = championId,
      lore = it.lore,
      blurb = it.blurb,
      tags = it.tags.map { ChampionTag.from(it) },
      partyTypeId = PartyType.from(it.partyType).dbId,
    )
  }
}

fun List<ChampionSkins>.toTable(championId: ChampionId): List<SkinTable> =
  this
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
