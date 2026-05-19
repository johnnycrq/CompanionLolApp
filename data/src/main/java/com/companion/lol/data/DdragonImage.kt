package com.companion.lol.data

import androidx.compose.runtime.Stable
import com.companion.lol.network.EndPoints
import com.companion.lol.storage.impl.model.ids.ChampionId

@Stable
sealed interface DdragonImage {
    @Stable
    val imageUrl: String

    @Stable
    val championId: ChampionId

    companion object {
        fun EMPTY(
            championId: ChampionId
        ): DdragonImage = Impl(championId, "")
        @Stable
        fun championSquareImage(championId: ChampionId, image: String): DdragonImage = Impl(
            championId = championId,
            imageUrl = EndPoints.DDragon.championSquareAsset(image)
        )

        @Stable
        fun championSkinImage(
            championId: ChampionId,
            keyName: String,
            skinNumber: Int
        ): DdragonImage = Impl(
            championId = championId,
            imageUrl = EndPoints.DDragon.championSkinAsset(
                keyName,
                skinNumber
            )
        )
    }
}

@Stable
private data class Impl(override val championId: ChampionId, override val imageUrl: String) :
    DdragonImage