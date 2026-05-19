package com.lol.app.ui.screens.championDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.companion.lol.data.DdragonImage
import com.companion.lol.data.model.other.ChampionSkin
import com.companion.lol.storage.impl.model.ids.ChampionId

@Stable
interface ChampionSkinImagesProvider {
    val image: DdragonImage

    fun toggleSkin()
}

private class Impl(
    private val noImage: DdragonImage,
): ChampionSkinImagesProvider{
    private var currentIndex by mutableIntStateOf(0)
    val skins = mutableStateOf<List<DdragonImage>>(emptyList())

    override val image: DdragonImage
        get() = skins.value.getOrNull(currentIndex)
            ?: noImage

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
fun rememberChampionSkinImageProvider(
    championId: ChampionId,
    skins: List<ChampionSkin>?
): ChampionSkinImagesProvider{
    return remember(championId) {
        Impl(
            noImage = DdragonImage.EMPTY(championId)
        )
    }.apply {
        this.skins.value = skins?.map { it.image } ?: emptyList()
    }
}
