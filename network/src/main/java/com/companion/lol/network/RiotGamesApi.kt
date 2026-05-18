package com.companion.lol.network

import com.companion.lol.network.dto.ChampionRotationsDto
import retrofit2.http.GET

interface RiotGamesApi{
    @GET("platform/v3/champion-rotations")
    suspend fun getChampionRotations(): ChampionRotationsDto
}