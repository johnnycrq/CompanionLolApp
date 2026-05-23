package com.lol.app.ui

import androidx.compose.runtime.Stable
import com.companion.lol.storage.impl.model.ids.ChampionId

@Stable
interface AppActions {
  fun goToChampionDetails(championId: ChampionId)
}
