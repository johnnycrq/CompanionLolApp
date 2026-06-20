package com.companion.lol.core.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import coil3.Image
import com.companion.lol.core.model.ChampionId

val LocalChampionColorCache =
  compositionLocalOf<ChampionColorCache> { error("Not initialized yet") }

@Stable
interface ChampionColorCache {

  val defaultColor: Color

  fun getColor(id: ChampionId): Color

  fun isDefaultColor(id: ChampionId): Boolean

  fun putColor(id: ChampionId, color: Color)

  fun extractColor(input: Image, championId: ChampionId)
}
