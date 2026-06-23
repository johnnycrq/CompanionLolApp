package com.companion.lol.domain.usecase

import com.companion.lol.domain.mapper.model
import com.companion.lol.domain.model.UserSettings
import com.companion.lol.domain.usecase.impl.ObserveSettings
import com.companion.lol.storage.impl.store.SettingsStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveSettingsImpl @Inject constructor(private val settingsStore: SettingsStore) :
  ObserveSettings {
  override operator fun invoke(): Flow<UserSettings> =
    settingsStore.observe().map { it?.model() ?: UserSettings.DEFAULT }
}
