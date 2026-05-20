package com.lol.app.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lol.app.base.CompanionAppPreview
import com.lol.app.base.CompanionAppPreviewWrapperProvider
import com.lol.app.base.material3.CompanionAppSurface

@Composable
fun LoginScreen(onLoginClicked: (emailAddress: String) -> Unit) {
  val viewModel: LoginViewModel = hiltViewModel()
  val state by viewModel.state.collectAsState()

  LoginScreen(
    state = state,
    onEmailChanged = viewModel::onEmailChanged,
    onLoginClicked = { onLoginClicked(state.email) },
  )
}

@Composable
fun LoginScreen(state: LoginState, onEmailChanged: (String) -> Unit, onLoginClicked: () -> Unit) {

  CompanionAppSurface(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      Column(
        modifier = Modifier.fillMaxWidth().weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = "Welcome",
          style = MaterialTheme.typography.displaySmall,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White,
          textAlign = TextAlign.Center,
        )

        Text(
          text = "Sign in to your account",
          style = MaterialTheme.typography.bodyLarge,
          color = Color.White.copy(alpha = 0.8f),
          textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
          value = state.email,
          onValueChange = onEmailChanged,
          label = { Text("Email Address", color = Color.White.copy(alpha = 0.7f)) },
          modifier = Modifier.padding(horizontal = 32.dp).fillMaxWidth(),
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors =
            OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color.White,
              unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              cursorColor = Color.White,
              focusedLabelColor = Color.White,
              unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
              focusedContainerColor = Color.White.copy(alpha = 0.1f),
              unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
            ),
          keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        )
      }

      Button(
        onClick = onLoginClicked,
        enabled = state.isEmailValid,
        modifier = Modifier.imePadding().padding(32.dp).fillMaxWidth().height(64.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White.copy(alpha = 0.3f),
            disabledContentColor = Color.White.copy(alpha = 0.5f),
          ),
      ) {
        Text(text = "Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
@CompanionAppPreview
@PreviewWrapper(CompanionAppPreviewWrapperProvider::class)
private fun LoginPreview() {
  LoginScreen(state = LoginState(email = ""), onEmailChanged = {}, onLoginClicked = {})
}

@Composable
@CompanionAppPreview
@PreviewWrapper(CompanionAppPreviewWrapperProvider::class)
private fun LoginPreview2() {
  LoginScreen(state = LoginState(), onEmailChanged = {}, onLoginClicked = {})
}
