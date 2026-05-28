@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.companion.lol.app.compose.animation.defaultNoTransition
import com.companion.lol.app.compose.animation.predictiveBack
import com.companion.lol.app.compose.ui.theme.CompanionAppTheme
import com.companion.lol.app.compose.utils.isLandscape
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.keys.ChampionDetailsKey
import com.companion.lol.app.navigation.keys.ChampionListKey
import com.companion.lol.app.navigation.keys.InitialScreenKey
import com.companion.lol.app.navigation.keys.LoginKey
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.navigation.keys.SettingsKey
import com.companion.lol.app.navigation.keys.entryScreenKey
import com.companion.lol.app.ui.scene.rememberBottomSheetSceneStrategy
import com.companion.lol.app.ui.scene.rememberNavigationBarDecoratorStrategy
import com.companion.lol.app.ui.screens.NavigationBar
import com.companion.lol.app.ui.screens.navigationBarHeight
import com.companion.lol.app.util.ChampionColorCache
import com.companion.lol.app.util.LocalChampionColorCache
import dagger.hilt.android.AndroidEntryPoint

val LocalContentPadding = compositionLocalOf { PaddingValues.Zero } // could be static?
val LocalBackStack = compositionLocalOf<BackStack<ScreenKey>> { error("Not initialized") }
val LocalSnackBarManager = compositionLocalOf<SnackBarManager> { error("Not initialized") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
    setContent { MainScreen() }
  }
}

@Composable
private fun MainScreen() {
  CompanionAppTheme {
    val viewModel = hiltViewModel<MainViewModel>()

    MainScreen(
      snackBarManager = viewModel.snackBarManager,
      colorCache = viewModel.colorCache,
      backStack = viewModel.backStack,
    )
  }
}

@Composable
private fun MainScreen(
  snackBarManager: SnackBarManager,
  colorCache: ChampionColorCache,
  backStack: BackStack<ScreenKey>,
) {
  snackBarManager.ShowSnackBarMessagesEffect { error ->
    showSnackbar(error.message, duration = SnackbarDuration.Short)
  }

  val isLandscape = isLandscape()
  val snackbarPadding =
    remember(backStack, isLandscape) {
      derivedStateOf {
        if (backStack.current.isNavBarEntry() && !isLandscape) {
          navigationBarHeight
        } else {
          0.dp
        }
      }
    }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.secondary,
    snackbarHost = {
      SnackbarHost(
        hostState = snackBarManager.snackBarHostState,
        snackbar = {
          Snackbar(
            modifier = Modifier.padding(bottom = snackbarPadding.value),
            snackbarData = it,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onBackground,
          )
        },
      )
    },
  ) { contentPadding ->
    NavDisplay(
      contentPadding = contentPadding,
      colorCache = colorCache,
      backStack = backStack,
      snackBarManager = snackBarManager,
    )
  }
}

@Composable
private fun NavDisplay(
  contentPadding: PaddingValues,
  colorCache: ChampionColorCache,
  backStack: BackStack<ScreenKey>,
  snackBarManager: SnackBarManager,
) {

  SharedTransitionLayout {
    CompositionLocalProvider(
      LocalContentPadding provides contentPadding,
      LocalChampionColorCache provides colorCache,
      LocalBackStack provides backStack,
      LocalSnackBarManager provides snackBarManager,
    ) {
      NavDisplay(
        backStack = backStack.history,
        onBack = backStack::goBack,
        entryDecorators =
          listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
          ),
        sceneStrategies = listOf(rememberBottomSheetSceneStrategy()),
        sceneDecoratorStrategies =
          listOf(
            rememberNavigationBarDecoratorStrategy(
              navBar = {
                NavigationBar(currentKey = { backStack.current }, goTo = backStack::goTo)
              },
              sharedTransitionScope = this,
            )
          ),
        transitionSpec = defaultNoTransition(),
        popTransitionSpec = defaultNoTransition(),
        predictivePopTransitionSpec = predictiveBack(),
        entryProvider =
          entryProvider {
            entryScreenKey<InitialScreenKey>()
            entryScreenKey<LoginKey>()
            entryScreenKey<ChampionListKey>()
            entryScreenKey<ChampionDetailsKey>()
            entryScreenKey<SettingsKey>()
          },
      )
    }
  }
}
