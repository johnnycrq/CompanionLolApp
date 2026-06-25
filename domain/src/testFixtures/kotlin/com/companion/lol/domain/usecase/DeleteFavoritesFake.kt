package com.companion.lol.domain.usecase

class DeleteFavoritesFake : DeleteFavorites {
  var callCount = 0

  override suspend fun invoke() {
    callCount++
  }
}
