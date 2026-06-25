package com.companion.lol.ui.details

import com.companion.lol.core.ui.MessagePoster
import com.companion.lol.core.ui.UiError

class MessagePosterFake : MessagePoster {
  var callCount = 0
  var lastMessage: UiError? = null

  override fun postMessage(message: UiError) {
    callCount++
    lastMessage = message
  }
}
