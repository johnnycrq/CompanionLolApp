package com.companion.lol.domain.usecase

import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.SortOrder
import com.companion.lol.storage.impl.store.SettingsStore
import javax.inject.Inject

class UpdateSettings @Inject constructor(private val settingsStore: SettingsStore) {
  suspend operator fun invoke(gridSize: GridSize) {
    settingsStore.insert(championGridSize = gridSize)
  }

  suspend operator fun invoke(sortOrder: SortOrder) {
    settingsStore.insert(championSortOrder = sortOrder)
  }
}
