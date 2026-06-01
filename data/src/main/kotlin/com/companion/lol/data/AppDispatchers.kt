package com.companion.lol.data

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

@Singleton
data class AppDispatchers(
  val io: CoroutineContext,
  val computation: CoroutineContext,
  val main: CoroutineContext,
) {
  @Inject constructor() : this(
      io = Dispatchers.IO,
      computation = Dispatchers.Default,
      main = Dispatchers.Main
  )
}
