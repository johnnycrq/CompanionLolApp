package com.companion.lol.app.navigation.keys

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Serializable
data object InitialScreenKey : ScreenKey {
  @Composable
  override fun Content() {
    Box(modifier = Modifier)
  }
}
