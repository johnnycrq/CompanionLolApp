package com.companion.lol.domain.mapper

import com.companion.lol.core.model.ChampionId
import com.companion.lol.network.dto.other.ChampionSkins
import com.companion.lol.storage.impl.model.ids.SkinId
import com.companion.lol.storage.sqldelight.tables.SkinTable

internal fun List<ChampionSkins>.table(championId: ChampionId): List<SkinTable> =
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
