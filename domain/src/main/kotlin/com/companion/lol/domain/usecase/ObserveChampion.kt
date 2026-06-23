package com.companion.lol.domain.usecase

import com.companion.lol.domain.model.Champion
import kotlinx.coroutines.flow.Flow

interface ObserveChampion {
  operator fun invoke(): Flow<List<Champion>>
}
