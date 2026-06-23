package com.companion.lol.domain.usecase

interface DeleteSession {
  suspend operator fun invoke(): Unit
}
