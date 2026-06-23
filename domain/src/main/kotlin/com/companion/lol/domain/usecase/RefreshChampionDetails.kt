package com.companion.lol.domain.usecase

import com.companion.lol.core.model.ChampionId
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface RefreshChampionDetails {
  suspend operator fun invoke(
    retry: Int = 3,
    retryDelay: Duration = 1.seconds,
    championId: ChampionId,
  ): Boolean
}
