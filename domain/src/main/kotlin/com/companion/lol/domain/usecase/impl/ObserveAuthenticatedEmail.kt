package com.companion.lol.domain.usecase.impl

import kotlinx.coroutines.flow.Flow

interface ObserveAuthenticatedEmail {
  operator fun invoke(): Flow<String?>
}
