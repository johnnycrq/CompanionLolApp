package com.companion.lol.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ChampionDto(
  val id: String,
  private val key: String,
  val name: String,
  val title: String,
  val blurb: String,
  val image: Image,
  @SerialName("partype") val partType: String,
) {
  @Transient val championKey: Int = key.toInt()

  @Serializable class Image(val full: String, val sprite: String)
}
