package com.lol.app.ui.screens.championDetails

import com.companion.lol.data.model.ChampionDetailsModel
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.base.ComposeState

data class ChampionDetailsState(
  val championId: ChampionId,
  val champion: ChampionModel? = null,
  val details: ChampionDetailsModel? = null,
) : ComposeState
