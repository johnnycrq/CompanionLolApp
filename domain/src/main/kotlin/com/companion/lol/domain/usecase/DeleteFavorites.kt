package com.companion.lol.domain.usecase

interface DeleteFavorites {
  suspend operator fun invoke(): Unit
}
