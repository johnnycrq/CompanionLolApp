package com.companion.lol.domain.usecase.impl

import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.SortOrder
import com.companion.lol.domain.usecase.UpdateSettings
import com.companion.lol.storage.impl.store.SettingsStore
import javax.inject.Inject

class UpdateSettingsImpl @Inject constructor(private val settingsStore: SettingsStore) :
  UpdateSettings {
  override suspend operator fun invoke(gridSize: GridSize?, sortOrder: SortOrder?) {
    settingsStore.insert(championGridSize = gridSize)
  }
}
