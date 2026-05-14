package com.lol.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

class BottomNavSceneStrategy(private val onGoTo: (ScreenKey) -> Unit) : SceneStrategy<ScreenKey> {
  override fun SceneStrategyScope<ScreenKey>.calculateScene(
    entries: List<NavEntry<ScreenKey>>
  ): Scene<ScreenKey>? {
    val currentEntry: NavEntry<ScreenKey> = entries.lastOrNull() ?: return null
    val screenKey: ScreenKey? = currentEntry.metadata["screen_key"] as? ScreenKey
    if (screenKey is MainBottomNavScreen) {
      return Scene(
        screenKey = screenKey,
        currentEntry = currentEntry,
        entries = entries,
        onGoTo = onGoTo,
      )
    }
    return null
  }
}

private class Scene(
  val screenKey: ScreenKey,
  val currentEntry: NavEntry<ScreenKey>,
  override val entries: List<NavEntry<ScreenKey>>,
  private val onGoTo: (ScreenKey) -> Unit,
) : Scene<ScreenKey> {
  override val key: Any = "BottomNavScene"
  override val previousEntries: List<NavEntry<ScreenKey>> = entries.dropLast(1)
  override val metadata: Map<String, Any> = currentEntry.metadata
  override val content: @Composable () -> Unit =
    @Composable {
      Box(contentAlignment = Alignment.BottomCenter) {
        Box(modifier = Modifier.padding(bottom = 80.dp)) { currentEntry.Content() }

        NavigationBar {
          NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
            label = { Text("Feature") },
            selected = screenKey is ChampionListKey,
            onClick = { onGoTo(ChampionListKey) },
          )

          NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            label = { Text("Settings") },
            selected = screenKey is SettingsKey,
            onClick = { onGoTo(SettingsKey) },
          )
        }
      }
    }
}
