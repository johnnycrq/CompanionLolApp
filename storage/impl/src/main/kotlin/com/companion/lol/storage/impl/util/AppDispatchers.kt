package com.companion.lol.storage.impl.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class AppDispatchers(
  val io: CoroutineDispatcher,
  val computation: CoroutineDispatcher,
  val main: CoroutineDispatcher,
) {
  @Inject constructor() : this(Dispatchers.IO, Dispatchers.Default, Dispatchers.Main)
}
