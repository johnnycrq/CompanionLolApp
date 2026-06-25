package com.companion.lol.domain.usecase

import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.SortOrder

class UpdateSettingsFake : UpdateSettings {
  var callCount = 0
  var lastGridSize: GridSize? = null
  var lastSortOrder: SortOrder? = null

  override suspend fun invoke(gridSize: GridSize?, sortOrder: SortOrder?) {
    callCount++
    lastGridSize = gridSize
    lastSortOrder = sortOrder
  }
}
