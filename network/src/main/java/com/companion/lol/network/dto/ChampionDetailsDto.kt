package com.companion.lol.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
class ChampionDetailsDto(private val data: HashMap<String, Data>) {
  val info: Data = data.values.first()

  @Serializable
  @Keep
  class Data(
    val key: String,
    val title: String,
    val lore: String,
    val blurb: String,
    val tags: List<String>,
    val partytype: String? = null,
    val stats: Stats,
    val skins: List<Skins>,
  )

  @Serializable
  @Keep
  class Stats(
    val hp: Int,
    val hpperlevel: Int,
    val mp: Int,
    val mpperlevel: Int,
    val movespeed: Int,
    val armor: Int,
    val armorperlevel: Float,
    val spellblock: Int,
    val spellblockperlevel: Float,
    val attackrange: Int,
    val hpregen: Float,
    val hpregenperlevel: Float,
    val mpregen: Float,
    val mpregenperlevel: Float,
    val crit: Int,
    val critperlevel: Int,
    val attackdamage: Int,
    val attackdamageperlevel: Float,
    val attackspeedperlevel: Float,
    val attackspeed: Float,
  )

  @Serializable
  @Keep
  class Skins(
    val id: Int,
    val num: Int,
    val name: String,
    val chromas: Boolean,
    val parentSkin: Int? = null,
  )
}
