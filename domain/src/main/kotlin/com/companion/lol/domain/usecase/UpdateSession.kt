package com.companion.lol.domain.usecase

interface UpdateSession {
  suspend operator fun invoke(emailAddress: String, autoSync: Boolean)

  suspend operator fun invoke(autoSync: Boolean)
}
