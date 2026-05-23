package com.lol.app.navigation.keys

import androidx.compose.runtime.Composable
import com.lol.app.navigation.BackStack
import com.lol.app.ui.screens.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
data object LoginKey : ScreenKey {
  @Composable
  override fun Content(backStack: BackStack<ScreenKey>) {
    LoginScreen()
  }
}
