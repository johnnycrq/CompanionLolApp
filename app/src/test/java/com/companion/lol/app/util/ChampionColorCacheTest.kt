package com.companion.lol.app.util

import androidx.compose.ui.graphics.Color
import com.companion.lol.storage.impl.model.ids.ChampionId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChampionColorCacheTest {
  private val defaultColor = Color.Red
  private val championId = ChampionId(1)
  private val blueColor = Color.Blue
  private val greenColor = Color.Green

  private val mockBitmap: ChampionBitmap =
    object : ChampionBitmap {
      override val bitmap: coil3.Bitmap
        get() = TODO("Not yet implemented")
    }

  private fun runUnconfinedTest(block: suspend TestScope.() -> Unit) =
    runTest(UnconfinedTestDispatcher(), testBody = block)

  private fun TestScope.createCache(extractColor: (ChampionBitmap) -> Color = { blueColor }) =
    ChampionColorCache.Impl(
      scope = backgroundScope,
      defaultColor = defaultColor,
      extractColor = extractColor,
    )

  @Test
  fun `getColor returns default color for new champion id`() = runUnconfinedTest {
    val cache = createCache()

    val color = cache.getColor(championId)

    assertEquals(defaultColor, color)
  }

  @Test
  fun `putColor updates the color for champion id`() = runUnconfinedTest {
    val cache = createCache()

    cache.putColor(championId, greenColor)

    assertEquals(greenColor, cache.getColor(championId))
  }

  @Test
  fun `isDefaultColor returns true when color has not been changed`() = runUnconfinedTest {
    val cache = createCache()

    assertTrue(cache.isDefaultColor(championId))
  }

  @Test
  fun `isDefaultColor returns false after putColor with non-default color`() = runUnconfinedTest {
    val cache = createCache()

    cache.putColor(championId, greenColor)

    assertFalse(cache.isDefaultColor(championId))
  }

  @Test
  fun `extractColor updates cache when current color is default`() = runUnconfinedTest {
    val cache = createCache { blueColor }

    cache.extractColor(mockBitmap, championId)

    assertEquals(blueColor, cache.getColor(championId))
  }

  @Test
  fun `extractColor does nothing when current color is already non-default`() = runUnconfinedTest {
    val cache = createCache { blueColor }
    cache.putColor(championId, greenColor)

    cache.extractColor(mockBitmap, championId)

    assertEquals(greenColor, cache.getColor(championId))
  }

  @Test
  fun `extractColor is called only once for consecutive calls`() = runTest {
    var callCount = 0
    val cache = createCache {
      callCount++
      blueColor
    }

    // Both calls happen before the background worker processes the first one
    cache.extractColor(mockBitmap, championId)
    cache.extractColor(mockBitmap, championId)

    // Trigger the background processing
    runCurrent()

    assertEquals(1, callCount)
  }
}
