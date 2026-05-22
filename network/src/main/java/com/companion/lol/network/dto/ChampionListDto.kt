package com.companion.lol.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Suppress("CanBeParameter")
class ChampionListDto(private val data: HashMap<String, ChampionDto>) {
  val champions: List<ChampionDto> = data.values.toList()
}
