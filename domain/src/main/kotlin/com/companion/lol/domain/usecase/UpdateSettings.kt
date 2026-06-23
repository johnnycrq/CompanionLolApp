package com.companion.lol.domain.usecase

import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.SortOrder

interface UpdateSettings {
  suspend operator fun invoke(gridSize: GridSize? = null, sortOrder: SortOrder? = null)
}
