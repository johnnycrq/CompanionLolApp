package com.companion.lol.data.mapper

import com.companion.lol.data.model.SettingsModel
import com.companion.lol.storage.sqldelight.tables.SettingsTable

fun SettingsTable.model() = SettingsModel(
    championRotationGridSize = this.championRotationGridSize,
    championRotationSortOrder = this.championRotationSortOrder
)