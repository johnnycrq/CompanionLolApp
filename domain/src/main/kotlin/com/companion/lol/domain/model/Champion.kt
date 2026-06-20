package com.companion.lol.domain.model

import androidx.compose.runtime.Immutable
import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.model.DdragonImage
import com.companion.lol.core.model.PartyType

@Immutable
data class Champion(
  val id: ChampionId,
  val name: String,
  val keyName: String,
  val title: String,
  val squareImage: DdragonImage,
  val partyType: PartyType,
  val isFavorite: Boolean,
)
