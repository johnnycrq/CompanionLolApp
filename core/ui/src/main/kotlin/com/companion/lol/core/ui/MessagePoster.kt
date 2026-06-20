package com.companion.lol.core.ui

import androidx.compose.runtime.Stable

@Stable
interface MessagePoster {
  fun postMessage(message: UiError)
}
