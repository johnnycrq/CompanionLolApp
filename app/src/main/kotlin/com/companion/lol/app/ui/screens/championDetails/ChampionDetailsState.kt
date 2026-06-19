package com.companion.lol.app.ui.screens.championDetails

import com.companion.lol.app.base.ComposeState
import com.companion.lol.core.model.ChampionId
import com.companion.lol.domain.model.Champion
import com.companion.lol.domain.model.ChampionDetails
import com.companion.lol.domain.model.ChampionWithDetails

data class ChampionDetailsState(
  val championId: ChampionId,
  val champion: Champion? = null,
  val details: ChampionDetails? = null,
) : ComposeState {

  constructor(
    championId: ChampionId,
    championWithDetails: ChampionWithDetails,
  ) : this(championId, championWithDetails.champion, championWithDetails.details)

  val hasData: Boolean = champion != null && details != null
}
