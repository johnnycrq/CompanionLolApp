package com.companion.lol.app.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.companion.lol.app.setContentWithTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun loginButton_isInitiallyDisabled() {
    composeTestRule.setContentWithTheme {
      LoginScreen(state = LoginState(), onEmailChanged = {}, onLoginClicked = {})
    }

    composeTestRule.onNodeWithText("Continue").assertIsNotEnabled()
  }

  @Test
  fun loginButton_updatesEnabledState_basedOnEmail() {
    var state by mutableStateOf(LoginState())
    composeTestRule.setContentWithTheme {
      LoginScreen(
        state = state,
        onEmailChanged = { state = state.copy(email = it) },
        onLoginClicked = {},
      )
    }

    val continueButton = composeTestRule.onNodeWithText("Continue")
    val emailField = composeTestRule.onNodeWithText("Email Address")

    continueButton.assertIsNotEnabled()

    emailField.performTextInput("invalid-email")
    continueButton.assertIsNotEnabled()

    emailField.performTextClearance()
    emailField.performTextInput("test@example.com")
    continueButton.assertIsEnabled()
  }

  @Test
  fun clickingLoginButton_triggersCallback() {
    var loginClicked = false
    composeTestRule.setContentWithTheme {
      LoginScreen(
        state = LoginState(email = "test@example.com"),
        onEmailChanged = {},
        onLoginClicked = { loginClicked = true },
      )
    }

    composeTestRule.onNodeWithText("Continue").performClick()
    assertTrue(loginClicked)
  }
}
