package com.companion.lol.app.ui.screens

data class DataRefreshState(
  val refreshing: Boolean = true,
  val userTriggered: Boolean = false,
  val hasError: Boolean = false,
)
