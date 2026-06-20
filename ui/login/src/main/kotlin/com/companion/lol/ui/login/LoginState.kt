package com.companion.lol.ui.login

import android.util.Patterns
import com.companion.lol.core.ui.state.ComposeState
import kotlinx.serialization.Serializable

@Serializable
data class LoginState(val email: String = "") : ComposeState {
  val isEmailValid: Boolean = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
