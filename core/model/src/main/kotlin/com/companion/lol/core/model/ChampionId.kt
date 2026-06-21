package com.companion.lol.core.model

import androidx.compose.runtime.Immutable
import com.companion.lol.core.serializer.ChampionIdSerializer
import kotlinx.serialization.Serializable

@Immutable @Serializable(ChampionIdSerializer::class) data class ChampionId(val value: Int)
