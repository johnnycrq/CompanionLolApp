package com.companion.lol.domain.usecase

import com.companion.lol.core.model.ChampionId
import kotlin.time.Duration

class RefreshChampionDetailsFake : RefreshChampionDetails {
  var callCount = 0
  var result: Boolean = true

  override suspend fun invoke(retry: Int, retryDelay: Duration, championId: ChampionId): Boolean {
    callCount++
    return result
  }
}
