package com.companion.lol.ui.champion

import com.companion.lol.domain.usecase.RefreshChampion

class RefreshChampionFake : RefreshChampion {
  var callCount = 0
  var lastForceRefresh: Boolean? = null
  var result: Boolean = true

  override suspend fun invoke(forceRefresh: Boolean): Boolean {
    callCount++
    lastForceRefresh = forceRefresh
    return result
  }
}
