package com.companion.lol.ui.details

import com.companion.lol.core.model.ChampionId
import com.companion.lol.domain.usecase.RefreshChampionDetails
import kotlin.time.Duration

class RefreshChampionDetailsFake : RefreshChampionDetails {
  var callCount = 0
  var result: Boolean = true

  override suspend fun invoke(retry: Int, retryDelay: Duration, championId: ChampionId): Boolean {
    callCount++
    return result
  }
}
