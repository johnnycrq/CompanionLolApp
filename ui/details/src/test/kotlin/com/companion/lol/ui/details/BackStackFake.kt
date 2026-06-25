package com.companion.lol.ui.details

import com.companion.lol.core.ui.navigation.BackStack
import com.companion.lol.core.ui.navigation.ScreenKey

class BackStackFake : BackStack {
  var goBackCount = 0

  override val history: List<ScreenKey> = emptyList()

  override val current: ScreenKey = object : ScreenKey {}

  override fun goTo(key: ScreenKey) {}

  override fun setHistory(singleKey: ScreenKey) {}

  override fun setHistory(newHistory: List<ScreenKey>) {}

  override fun goBack(): Boolean {
    goBackCount++
    return true
  }
}
