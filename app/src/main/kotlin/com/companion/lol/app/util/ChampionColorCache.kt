package com.companion.lol.app.util

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil3.Bitmap
import com.companion.lol.storage.impl.model.ids.ChampionId
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val LocalChampionColorCache =
  compositionLocalOf<ChampionColorCache> { error("Not initialized yet") }

@Stable interface ChampionColorCache : ChampionColorCacheExtractor<Bitmap>

private val colorExtractor: (Bitmap) -> Color = { input ->
  Color(
    Palette.from(input).generate().let {
      it.getVibrantColor(0).takeIf { color -> color != 0 } ?: it.getDominantColor(0)
    }
  )
}

fun CoroutineScope.ChampionColorCache(
  extractContext: CoroutineContext,
  defaultColor: Color,
): ChampionColorCache =
  object :
    ChampionColorCache,
    ChampionColorCacheExtractor<Bitmap> by ChampionColorCacheExtractor.Impl(
      scope = this,
      extractContext = extractContext,
      defaultColor = defaultColor,
      extractColor = colorExtractor,
    ) {}

@Stable
interface ChampionColorCacheExtractor<T : Any> {

  val defaultColor: Color

  fun getColor(id: ChampionId): Color

  fun isDefaultColor(id: ChampionId): Boolean

  fun putColor(id: ChampionId, color: Color)

  fun extractColor(input: T, championId: ChampionId)

  @VisibleForTesting
  class Impl<T : Any>(
    scope: CoroutineScope,
    extractContext: CoroutineContext,
    override val defaultColor: Color,
    private val extractColor: (T) -> Color,
  ) : ChampionColorCacheExtractor<T> {
    private val cache = hashMapOf<ChampionId, MutableState<Color>>()
    private val extractChannel = Channel<Pair<ChampionId, T>>(capacity = Channel.UNLIMITED)

    init {
      scope.launch {
        for ((championId, input) in extractChannel) {
          if (!isDefaultColor(championId)) continue
          val color = withContext(extractContext) { extractColor(input) }
          putColor(id = championId, color = color)
        }
      }
    }

    private fun getColorState(id: ChampionId): MutableState<Color> =
      cache.getOrPut(id) { mutableStateOf(defaultColor) }

    override fun getColor(id: ChampionId): Color = getColorState(id).value

    override fun putColor(id: ChampionId, color: Color) {
      getColorState(id).value = color
    }

    override fun isDefaultColor(id: ChampionId): Boolean = getColor(id) == defaultColor

    override fun extractColor(input: T, championId: ChampionId) {
      if (!isDefaultColor(championId)) return

      extractChannel.trySend(championId to input)
    }
  }
}
