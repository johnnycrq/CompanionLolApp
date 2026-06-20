package com.companion.lol.domain.model

import androidx.compose.runtime.Stable
import com.companion.lol.core.model.DdragonImage
import com.companion.lol.storage.impl.model.ids.SkinId

@Stable
data class ChampionSkin(
  val skinId: SkinId,
  val name: String,
  val isChroma: Boolean,
  val image: DdragonImage.Skin,
)
