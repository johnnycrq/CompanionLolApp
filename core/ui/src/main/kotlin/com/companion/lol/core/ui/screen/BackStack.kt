package com.companion.lol.core.ui.screen

import androidx.compose.runtime.Stable

@Stable
interface BackStack<S : ScreenKey> {

  val history: List<S>

  val current: S

  fun goTo(key: S)

  fun setHistory(singleKey: S)

  fun setHistory(newHistory: List<S>)

  fun goBack(): Boolean
}
