package com.companion.lol.network.dto

import kotlinx.serialization.Serializable

@Serializable
@Suppress("CanBeParameter")
class ChampionListDto(
    private val data: HashMap<String, ChampionDto>
){
    val champions: List<ChampionDto> = data.values.toList()
}