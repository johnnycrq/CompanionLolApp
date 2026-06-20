package com.companion.lol.core.io

import kotlin.coroutines.CoroutineContext

data class AppDispatchers(
  val io: CoroutineContext,
  val computation: CoroutineContext,
  val main: CoroutineContext,
)
