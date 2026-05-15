package com.lol.app.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lol.app.base.material3.CompanionAppSurface

@Composable
fun SettingsScreen(onLogoutClicked: () -> Unit) {
  val viewModel: SettingsViewModel = hiltViewModel()

  CompanionAppSurface(modifier = Modifier.fillMaxSize()) {
    Box(
      modifier = Modifier.fillMaxSize().padding(bottom = 32.dp),
      contentAlignment = Alignment.BottomCenter,
    ) {
      Button(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        onClick = onLogoutClicked,
        colors =
          ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
          ),
      ) {
        Text(text = "LOGOUT")
      }
    }
  }
}
