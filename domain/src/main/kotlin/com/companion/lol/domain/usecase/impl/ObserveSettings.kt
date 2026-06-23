package com.companion.lol.domain.usecase.impl

import com.companion.lol.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface ObserveSettings {
  operator fun invoke(): Flow<UserSettings>
}
