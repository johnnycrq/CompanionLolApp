package com.companion.lol.domain.mapper

import com.companion.lol.domain.model.UserSettings
import com.companion.lol.storage.sqldelight.tables.SettingsTable

internal fun SettingsTable.model(): UserSettings =
  UserSettings(championGridSize = this.championGridSize, championSortOrder = this.championSortOrder)
