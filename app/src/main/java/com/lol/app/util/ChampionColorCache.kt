package com.lol.app.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.companion.lol.storage.impl.model.ids.ChampionId

@Stable
object ChampionColorCache {
    private val cache = hashMapOf<ChampionId, MutableState<Color?>>()
    @Stable
    fun getColor(id: ChampionId): MutableState<Color?> =
        cache.getOrPut(id) { mutableStateOf(null) }

    fun putColor(id: ChampionId, color: Color) {
        getColor(id).value = color
    }
}