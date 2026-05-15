package com.lol.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.lol.app.base.theme.CompanionAppTheme
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.LoginKey
import com.lol.app.navigation.ScreenKey
import com.lol.app.navigation.SettingsKey
import com.lol.app.ui.screens.championList.ChampionListScreen
import com.lol.app.ui.screens.login.LoginScreen
import com.lol.app.ui.screens.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint

val LocalContentPadding = compositionLocalOf { PaddingValues.Zero }

private val contentTransformScreenTransition =
  ContentTransform(EnterTransition.None, ExitTransition.None)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      CompanionAppTheme {
        // viewModel not stable because it is read from outside
        val backStack = remember(viewModel) { viewModel.backStack }

        Scaffold(
          containerColor = Transparent,
          contentColor = MaterialTheme.colorScheme.onSurface,
        ) { contentPadding ->
          CompositionLocalProvider(LocalContentPadding provides contentPadding) {
            NavDisplay(
              backStack = backStack.history,
              onBack = backStack::goBack,
              entryDecorators =
                listOf(
                  // required otherwise savedState is scoped to activity not NavEntry
                  rememberSaveableStateHolderNavEntryDecorator(),
                  // required otherwise hiltViewModel is scoped to activity not NavEntry
                  rememberViewModelStoreNavEntryDecorator(),
                  rememberBottomNavEntryDecorator(backStack),
                ),
              transitionSpec = { contentTransformScreenTransition },
              popTransitionSpec = { contentTransformScreenTransition },
              predictivePopTransitionSpec = { contentTransformScreenTransition },
            ) { key: ScreenKey ->
              NavEntry(key = key, metadata = mapOf("screen_key" to key)) {
                when (key) {
                  InitialScreenKey -> PlaceHolderScreen()
                  LoginKey -> LoginScreen(onLoginClicked = viewModel::onLoginClicked)
                  ChampionListKey -> ChampionListScreen()
                  SettingsKey -> SettingsScreen(onLogoutClicked = viewModel::onLogoutClicked)
                }
              }
            }
          }
        }
      }
    }
  }

  @Composable
  private fun PlaceHolderScreen() {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary))
  }
}
