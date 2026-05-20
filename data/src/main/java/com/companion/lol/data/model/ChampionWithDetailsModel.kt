package com.companion.lol.data.model

import androidx.compose.runtime.Stable

@Stable
data class ChampionWithDetailsModel(
  val champion: ChampionModel? = null,
  val details: ChampionDetailsModel? = null,
)
