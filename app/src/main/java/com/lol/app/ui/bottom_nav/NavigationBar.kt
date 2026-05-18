package com.lol.app.ui.bottom_nav

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.companion.lol.app.R
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.ScreenKey
import com.lol.app.navigation.SettingsKey

@Composable
fun NavigationBar(
  backStack: BackStack<ScreenKey>
){
  val screenKey = backStack.current

  NavigationBar(
    containerColor = Color.Transparent,
    modifier = Modifier.background(
      brush =
        Brush.verticalGradient(
          colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
        )
    )
  ) {
    val colors = NavigationBarItemDefaults.colors(
      unselectedIconColor = MaterialTheme.colorScheme.secondary,
      unselectedTextColor = MaterialTheme.colorScheme.secondary,
      indicatorColor = MaterialTheme.colorScheme.secondary,
    )

    NavigationBarItem(
      icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
      label = { Text(stringResource(R.string.champion_rotation)) },
      selected = screenKey is ChampionListKey,
      colors = colors,
      onClick = { backStack.goTo(ChampionListKey) },
    )

    NavigationBarItem(
      icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
      label = { Text(stringResource(R.string.settings)) },
      selected = screenKey is SettingsKey,
      colors = colors,
      onClick = { backStack.goTo(SettingsKey) },
    )
  }
}