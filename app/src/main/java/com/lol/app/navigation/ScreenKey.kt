package com.lol.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.EntryProviderScope
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed interface ScreenKey {

  val screenType: ScreenType
    get() = ScreenType.NORMAL

  enum class ScreenType {
    NORMAL,
    BOTTOM_SHEET,
    DIALOG,
  }

  companion object {
    const val METADATA_KEY = "ScreenKey.METADATA_KEY"
  }
}

inline fun <reified K : ScreenKey> EntryProviderScope<ScreenKey>.entryScreenKey(
  metadata: Map<String, Any> = emptyMap(),
  noinline content: @Composable (K) -> Unit,
) {
  entry(
    metadata = { key ->
      buildMap {
        put(ScreenKey.METADATA_KEY, key)
        putAll(metadata)
      }
    },
    content = content,
  )
}
