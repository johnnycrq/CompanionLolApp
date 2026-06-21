package com.companion.lol.core.ui.navigation

import androidx.compose.runtime.Stable

@Stable
interface BackStack {

  val history: List<ScreenKey>

  val current: ScreenKey

  fun goTo(key: ScreenKey)

  fun setHistory(singleKey: ScreenKey)

  fun setHistory(newHistory: List<ScreenKey>)

  fun goBack(): Boolean
}
