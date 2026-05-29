package com.companion.lol.app.navigation.keys

import androidx.compose.runtime.Composable
import com.companion.lol.app.navigation.ScreenMetadata
import com.companion.lol.app.ui.screens.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsKey : ScreenKey {
  override fun requiresAuth(): Boolean = true

  override val metadata: Map<String, Any> = ScreenMetadata.topLevelDestination()

  @Composable
  override fun Content() {
    SettingsScreen()
  }
}
