package com.companion.lol.domain.model

import androidx.compose.runtime.Immutable
import com.companion.lol.core.model.ChampionTag

@Immutable
data class ChampionDetails(
  val lore: String,
  val blurb: String,
  val tags: List<ChampionTag>,
  val skins: List<ChampionSkin>,
)
