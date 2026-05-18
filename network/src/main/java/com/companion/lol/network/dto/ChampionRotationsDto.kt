package com.companion.lol.network.dto

import kotlinx.serialization.Serializable

@Serializable
class ChampionRotationsDto(
    val freeChampionIds: List<Int>
)