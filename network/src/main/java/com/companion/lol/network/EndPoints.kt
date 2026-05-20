package com.companion.lol.network

object EndPoints {
  object DDragon {
    private const val version = "13.6.1"
    private const val baseEndPoint = "https://ddragon.leagueoflegends.com/cdn"
    private const val baseEndPointWithVersion = "${baseEndPoint}/${version}"
    const val base = "${baseEndPointWithVersion}/data/en_US/"

    fun championSquareAsset(imageName: String) =
      "${baseEndPointWithVersion}/img/champion/${imageName}"

    fun championSkinAsset(championName: String, skinNumber: Int) =
      "${baseEndPoint}/img/champion/splash/${championName}_${skinNumber}.jpg"
  }
}
