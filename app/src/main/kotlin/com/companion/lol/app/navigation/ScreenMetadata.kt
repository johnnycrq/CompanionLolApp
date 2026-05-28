@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata
import com.companion.lol.app.navigation.keys.ScreenKey

object ScreenMetadata {
  object ScreenId : NavMetadataKey<ScreenKey>

  object BottomSheet : NavMetadataKey<ModalBottomSheetProperties>

  object NavBarEntry : NavMetadataKey<Boolean>

  fun screenKey(key: ScreenKey) = metadata { put(ScreenId, key) }

  fun navBarEntry() = metadata { put(NavBarEntry, true) }

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
