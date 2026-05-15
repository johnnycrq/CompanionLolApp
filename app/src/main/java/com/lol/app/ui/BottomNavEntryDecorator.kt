package com.lol.app.ui

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntryDecorator
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.MainBottomNavScreen
import com.lol.app.navigation.ScreenKey
import com.lol.app.navigation.SettingsKey

@Composable
fun <T : Any> rememberBottomNavEntryDecorator(
  backStack: BackStack<ScreenKey>
): NavEntryDecorator<T> = remember(backStack) { BottomNavEntryDecorator(backStack) }

private class BottomNavEntryDecorator<T : Any>(backStack: BackStack<ScreenKey>) :
  NavEntryDecorator<T>(
    onPop = {},
    decorate = { entry ->
      val screenKey: ScreenKey? = entry.metadata["screen_key"] as? ScreenKey
      if (screenKey is MainBottomNavScreen) {
        Box(contentAlignment = Alignment.BottomCenter) {
          Box(modifier = Modifier.padding(bottom = 80.dp)) { entry.Content() }

          NavigationBar {
            NavigationBarItem(
              icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
              label = { Text("Feature") },
              selected = screenKey is ChampionListKey,
              onClick = { backStack.goTo(ChampionListKey) },
            )

            NavigationBarItem(
              icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
              label = { Text("Settings") },
              selected = screenKey is SettingsKey,
              onClick = { backStack.goTo(SettingsKey) },
            )
          }
        }
      } else {
        entry.Content()
      }
    },
  )
