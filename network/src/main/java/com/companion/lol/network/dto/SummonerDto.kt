package com.companion.lol.network.dto

import kotlinx.serialization.Serializable

@Serializable
class SummonerDto(
    val id: String,
    val accountId: String,
    val name: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val summonerLevel: Int
)