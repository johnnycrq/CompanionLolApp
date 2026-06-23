package com.companion.lol.ui.champion

import com.companion.lol.domain.model.Champion
import com.companion.lol.domain.usecase.ObserveChampion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class ObserveChampionFake : ObserveChampion {
  private val flow = MutableSharedFlow<List<Champion>>(replay = 1)

  override fun invoke(): Flow<List<Champion>> = flow

  suspend fun emit(champions: List<Champion>) {
    flow.emit(champions)
  }
}
