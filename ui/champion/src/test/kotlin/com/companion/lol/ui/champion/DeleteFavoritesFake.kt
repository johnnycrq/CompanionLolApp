package com.companion.lol.ui.champion

import com.companion.lol.domain.usecase.DeleteFavorites

class DeleteFavoritesFake : DeleteFavorites {
  var callCount = 0

  override suspend fun invoke() {
    callCount++
  }
}
