package com.companion.lol.data

import androidx.compose.runtime.Stable
import com.companion.lol.network.EndPoints
import com.companion.lol.storage.impl.model.ids.ChampionId

@Stable
sealed interface DdragonImage {
  @Stable val imageUrl: String

  @Stable val championId: ChampionId

  @Stable val skinName: String?

  companion object {
    @Stable
    fun championSquareImage(championId: ChampionId, image: String): DdragonImage =
      Impl(
        championId = championId,
        imageUrl = EndPoints.DDragon.championSquareAsset(image),
        skinName = null,
      )

    @Stable
    fun championSkinImage(
      championId: ChampionId,
      keyName: String,
      skinNumber: Int,
      skinName: String,
    ): DdragonImage =
      Impl(
        championId = championId,
        imageUrl = EndPoints.DDragon.championSkinAsset(keyName, skinNumber),
        skinName = skinName,
      )
  }
}

@Stable
private data class Impl(
  override val championId: ChampionId,
  override val imageUrl: String,
  override val skinName: String?,
) : DdragonImage
