@file:Suppress("ClassName")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.lol.app.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.EntryProviderScope
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed interface ScreenKey {

  val screenType: ScreenType
    get() = ScreenType.Normal

  val requiresAuth: Boolean

  sealed interface ScreenType {
    data object Normal : ScreenType

    data class BottomSheet(
      val properties: ModalBottomSheetProperties =
        ModalBottomSheetProperties(
          isAppearanceLightStatusBars = false,
          isAppearanceLightNavigationBars = false,
          shouldDismissOnBackPress = true,
          shouldDismissOnClickOutside = true,
        )
    ) : ScreenType

    data object Dialog : ScreenType
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
