package com.companion.lol.app.ui.screens.championDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.companion.lol.data.io.images.DdragonImage
import com.companion.lol.data.model.other.ChampionSkin
import com.companion.lol.storage.impl.model.ids.ChampionId

@Stable
interface ChampionSkinProvider {
  val image: DdragonImage.Skin?

  fun toggleSkin()
}

private class Impl : ChampionSkinProvider {
  private var currentIndex by mutableIntStateOf(0)
  val skins = mutableStateOf<List<DdragonImage.Skin>>(emptyList())

  override val image: DdragonImage.Skin?
    get() = skins.value.getOrNull(currentIndex)

  override fun toggleSkin() {
    currentIndex = nextIndex()
  }

  private fun nextIndex(): Int {
    return if (currentIndex == skins.value.lastIndex) {
      0
    } else {
      currentIndex + 1
    }
  }
}

@Composable
fun rememberChampionSkinProvider(
  championId: ChampionId,
  skins: List<ChampionSkin>?,
): ChampionSkinProvider {
  return remember(championId) { Impl() }
    .apply { this.skins.value = skins?.map { it.image } ?: emptyList() }
}
