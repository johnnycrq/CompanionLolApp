package com.companion.lol.domain.model

sealed interface UserSession {
  data class Authenticated(val emailAddress: String, val autoSync: Boolean) : UserSession

  data object LoggedOut : UserSession
}
