package com.companion.lol.ui.champion

data class ChampionRefreshState(
  val refreshing: Boolean = true,
  val userTriggered: Boolean = false,
  val hasError: Boolean = false,
)
