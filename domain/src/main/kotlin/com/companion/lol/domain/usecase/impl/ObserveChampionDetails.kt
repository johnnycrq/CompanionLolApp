package com.companion.lol.domain.usecase.impl

import com.companion.lol.core.model.ChampionId
import com.companion.lol.domain.model.ChampionWithDetails
import kotlinx.coroutines.flow.Flow

interface ObserveChampionDetails {
  operator fun invoke(championId: ChampionId): Flow<ChampionWithDetails>
}
