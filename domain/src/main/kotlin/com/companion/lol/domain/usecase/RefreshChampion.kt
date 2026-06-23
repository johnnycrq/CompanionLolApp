package com.companion.lol.domain.usecase

interface RefreshChampion {
  suspend operator fun invoke(forceRefresh: Boolean): Boolean
}
