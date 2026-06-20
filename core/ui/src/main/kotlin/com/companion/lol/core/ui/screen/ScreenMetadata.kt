@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.core.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata

object ScreenMetadata {
  data object ScreenId : NavMetadataKey<ScreenKey>

  data object BottomSheet : NavMetadataKey<ModalBottomSheetProperties>

  data object TopLevelDestination : NavMetadataKey<Boolean>

  fun screenKey(key: ScreenKey) = metadata { put(ScreenId, key) }

  fun topLevelDestination() = metadata { put(TopLevelDestination, true) }

  fun bottomSheet(
    properties: ModalBottomSheetProperties =
      ModalBottomSheetProperties(
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false,
        shouldDismissOnBackPress = true,
        shouldDismissOnClickOutside = true,
      )
  ) = metadata { put(BottomSheet, properties) }
}
