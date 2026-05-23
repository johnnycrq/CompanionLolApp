@file:Suppress("ClassName")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.lol.app.navigation.keys

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.EntryProviderScope
import com.lol.app.navigation.BackStack
import com.lol.app.ui.LocalBackStack
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed interface ScreenKey {

  fun type(): Type {
    return Type.Normal
  }

  fun requiresAuth(): Boolean {
    return false
  }

  fun isNavBarEntry(): Boolean {
    return false
  }

  @Composable fun Content(backStack: BackStack<ScreenKey>)

  sealed interface Type {
    data object Normal : Type

    data class BottomSheet(
      val properties: ModalBottomSheetProperties =
        ModalBottomSheetProperties(
          isAppearanceLightStatusBars = false,
          isAppearanceLightNavigationBars = false,
          shouldDismissOnBackPress = true,
          shouldDismissOnClickOutside = true,
        )
    ) : Type

    data object Dialog : Type
  }

  companion object {
    const val METADATA_KEY = "ScreenKey.METADATA_KEY"
  }
}

inline fun <reified K : ScreenKey> EntryProviderScope<ScreenKey>.entryScreenKey(
  metadata: Map<String, Any> = emptyMap()
) {
  entry(
    metadata = { key ->
      buildMap {
        put(ScreenKey.METADATA_KEY, key)
        putAll(metadata)
      }
    },
    content = { key: K -> key.Content(LocalBackStack.current) },
  )
}
