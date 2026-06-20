package com.companion.lol.core.ui

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class UiError(val message: String) {
  val id: Long = UUID.randomUUID().mostSignificantBits
}
