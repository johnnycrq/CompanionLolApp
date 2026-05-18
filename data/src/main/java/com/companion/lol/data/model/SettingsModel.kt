package com.companion.lol.data.model

import com.companion.lol.storage.impl.model.other.SortOrder

data class SettingsModel(
    val championRotationGridSize: Int,
    val championRotationSortOrder: SortOrder,
)