package com.companion.lol.network

import com.companion.lol.network.dto.ChampionDetailsDto
import com.companion.lol.network.dto.ChampionListDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DDragonApi {
  @GET("champion.json") suspend fun getChampionList(): ChampionListDto

  @GET("champion/{championName}.json")
  suspend fun getChampionDetails(@Path("championName") championName: String): ChampionDetailsDto
}
