package com.companion.lol.ui.details

import com.companion.lol.core.model.ChampionId
import com.companion.lol.domain.model.ChampionWithDetails
import com.companion.lol.domain.usecase.impl.ObserveChampionDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class ObserveChampionDetailsFake : ObserveChampionDetails {
  private val flow = MutableSharedFlow<ChampionWithDetails>(replay = 1)

  override fun invoke(championId: ChampionId): Flow<ChampionWithDetails> = flow

  suspend fun emit(championWithDetails: ChampionWithDetails) {
    flow.emit(championWithDetails)
  }
}
