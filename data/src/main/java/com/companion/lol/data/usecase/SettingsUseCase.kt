package com.companion.lol.data.usecase

import com.companion.lol.data.mapper.model
import com.companion.lol.data.model.SettingsModel
import com.companion.lol.storage.impl.model.other.SortOrder
import com.companion.lol.storage.impl.store.SettingsStore
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.SettingsTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class SettingsUseCase @Inject constructor(private val settingsStore: SettingsStore) {
  fun observe(): Flow<SettingsModel> = settingsStore.observe().map(SettingsTable::model)

  suspend fun updateChampionGridSize(value: Int) = withDbContext {
    settingsStore.insert(championRotationGridSize = value)
  }

  suspend fun updateChampionSortOrder(sortOrder: SortOrder) = withDbContext {
    settingsStore.insert(championRotationSortOrder = sortOrder)
  }
}
