package com.companion.lol.app.util

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.companion.lol.app.compose.ui.theme.Gold1
import com.companion.lol.storage.impl.model.ids.ChampionId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

interface ChampionBitmap {
  val bitmap: coil3.Bitmap

  class Impl(override val bitmap: coil3.Bitmap) : ChampionBitmap
}

val LocalChampionColorCache =
  compositionLocalOf<ChampionColorCache> { error("Not initialized yet") }

@Stable
interface ChampionColorCache {

  val defaultColor: Color

  fun getColor(id: ChampionId): Color

  fun isDefaultColor(id: ChampionId): Boolean

  fun putColor(id: ChampionId, color: Color)

  fun extractColor(input: coil3.Bitmap, championId: ChampionId)

  class Impl(
    scope: CoroutineScope,
    override val defaultColor: Color = Gold1,
    private val extractColor: (ChampionBitmap) -> Color = { input ->
      Color(
        Palette.from(input.bitmap).generate().let {
          it.getVibrantColor(0).takeIf { color -> color != 0 } ?: it.getDominantColor(0)
        }
      )
    },
  ) : ChampionColorCache {
    private val cache = hashMapOf<ChampionId, MutableState<Color>>()
    private val extractChannel =
      Channel<Pair<ChampionId, ChampionBitmap>>(capacity = Channel.UNLIMITED)

    init {
      scope.launch {
        for ((championId, input) in extractChannel) {
          if (!isDefaultColor(championId)) continue
          putColor(id = championId, color = extractColor(input))
        }
      }
    }

    private fun getColorState(id: ChampionId): MutableState<Color> =
      cache.getOrPut(id) { mutableStateOf(defaultColor) }

    override fun getColor(id: ChampionId): Color {
      return getColorState(id).value
    }

    override fun putColor(id: ChampionId, color: Color) {
      getColorState(id).value = color
    }

    override fun isDefaultColor(id: ChampionId): Boolean = getColor(id) == defaultColor

    @VisibleForTesting
    fun extractColor(input: ChampionBitmap, championId: ChampionId) {
      if (!isDefaultColor(championId)) return

      extractChannel.trySend(championId to input)
    }

    override fun extractColor(input: coil3.Bitmap, championId: ChampionId) {
      extractColor(ChampionBitmap.Impl(input), championId)
    }
  }
}
