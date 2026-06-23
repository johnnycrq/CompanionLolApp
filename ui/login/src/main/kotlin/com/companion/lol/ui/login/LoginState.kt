package com.companion.lol.ui.login

import com.companion.lol.core.ui.state.ComposeState
import kotlinx.serialization.Serializable
import org.apache.commons.validator.routines.EmailValidator

private val emailValidator = EmailValidator.getInstance()

@Serializable
data class LoginState(val email: String = "") : ComposeState {
  val isEmailValid: Boolean = email.isNotEmpty() && emailValidator.isValid(email)
}
