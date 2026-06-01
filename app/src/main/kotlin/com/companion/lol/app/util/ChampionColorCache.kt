package com.companion.lol.app.util

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil3.Image
import coil3.toBitmap
import com.companion.lol.storage.impl.model.ids.ChampionId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

val LocalChampionColorCache =
  compositionLocalOf<ChampionColorCache> { error("Not initialized yet") }

private val colorExtractor: (Image) -> Result<Color> = { input ->
  runCatching {
    Color(
      Palette.from(input.toBitmap()).generate().let {
        it.getVibrantColor(0).takeIf { color -> color != 0 } ?: it.getDominantColor(0)
      }
    )
  }
}

fun CoroutineScope.ChampionColorCache(
  extractContext: CoroutineContext,
  defaultColor: Color,
): ChampionColorCache =
  ChampionColorCache.Impl(
    scope = this,
    extractContext = extractContext,
    defaultColor = defaultColor,
    extractColor = colorExtractor,
  )

@Stable
interface ChampionColorCache {

  val defaultColor: Color

  fun getColor(id: ChampionId): Color

  fun isDefaultColor(id: ChampionId): Boolean

  fun putColor(id: ChampionId, color: Color)

  fun extractColor(input: Image, championId: ChampionId)

  @VisibleForTesting
  class Impl(
    scope: CoroutineScope,
    extractContext: CoroutineContext,
    override val defaultColor: Color,
    private val extractColor: (Image) -> Result<Color>,
  ) : ChampionColorCache {
    private val cache = hashMapOf<ChampionId, MutableState<Color>>()
    private val extractChannel = Channel<Pair<ChampionId, Image>>(capacity = Channel.UNLIMITED)

    init {
      scope.launch {
        for ((championId, input) in extractChannel) {
          if (!isDefaultColor(championId)) continue
          val color = withContext(extractContext) { extractColor(input) }
          color
            .onSuccess { color -> putColor(id = championId, color = color) }
            .onFailure { e -> Timber.e(e, "Failed to extract color") }
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

    override fun extractColor(input: Image, championId: ChampionId) {
      if (!isDefaultColor(championId)) return

      extractChannel.trySend(championId to input)
    }
  }
}
