@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.ui.scene

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import com.companion.lol.app.compose.utils.isLandscape
import com.companion.lol.app.navigation.ScreenMetadata

/** An [OverlayScene] that renders an [entry] within a [ModalBottomSheet]. */
private data class BottomSheetScene<T : Any>(
  override val key: T,
  override val previousEntries: List<NavEntry<T>>,
  override val overlaidEntries: List<NavEntry<T>>,
  private val entry: NavEntry<T>,
  private val modalBottomSheetProperties: ModalBottomSheetProperties,
  private val onBack: () -> Unit,
) : OverlayScene<T> {

  override val entries: List<NavEntry<T>> = listOf(entry)

  override val content: @Composable (() -> Unit) = {
    val lifecycleOwner = rememberLifecycleOwner()
    ModalBottomSheet(
      onDismissRequest = onBack,
      properties = modalBottomSheetProperties,
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      sheetMaxWidth = if (isLandscape()) Dp.Unspecified else BottomSheetDefaults.SheetMaxWidth,
      contentWindowInsets = { WindowInsets(0) },
      containerColor = MaterialTheme.colorScheme.surface,
      dragHandle = null,
    ) {
      Box(contentAlignment = Alignment.TopCenter) {
        CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) { entry.Content() }
        BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}

/**
 * A [SceneStrategy] that displays entries that have added [bottomSheet] to their
 * [NavEntry.metadata] within a [ModalBottomSheet] instance.
 *
 * This strategy should always be added before any non-overlay scene strategies.
 */
class BottomSheetSceneStrategy<T : Any>(private val windowSizeClass: WindowSizeClass) :
  SceneStrategy<T> {
  override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
    if (windowSizeClass.isLandscape()) return null

    val lastEntry: NavEntry<T> = entries.lastOrNull() ?: return null

    val properties: ModalBottomSheetProperties =
      lastEntry.metadata[ScreenMetadata.BottomSheet] ?: return null

    @Suppress("UNCHECKED_CAST")
    return BottomSheetScene(
      key = lastEntry.contentKey as T,
      previousEntries = entries.dropLast(1),
      overlaidEntries = entries.dropLast(1),
      entry = lastEntry,
      modalBottomSheetProperties = properties,
      onBack = onBack,
    )
  }
}

@Composable
fun <T : Any> rememberBottomSheetSceneStrategy(): SceneStrategy<T> {
  val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
  return remember(windowSizeClass) { BottomSheetSceneStrategy(windowSizeClass) }
}
