package com.companion.lol.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.companion.lol.app.compose.ui.theme.CompanionAppTheme

fun ComposeContentTestRule.setContentWithTheme(content: @Composable () -> Unit) {
  setContent { CompanionAppTheme(content = content) }
}
