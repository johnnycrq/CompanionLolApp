package com.companion.lol.domain.model

import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.SortOrder

data class UserSettings(val championGridSize: GridSize, val championSortOrder: SortOrder) {
  companion object {
    val DEFAULT =
      UserSettings(championGridSize = GridSize.MEDIUM, championSortOrder = SortOrder.ASC)
  }
}
