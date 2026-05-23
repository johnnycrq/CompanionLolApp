package com.companion.lol.storage.impl.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val dbDispatcher: CoroutineDispatcher = Dispatchers.IO

suspend fun <T> withDbContext(block: suspend CoroutineScope.() -> T): T =
  withContext(dbDispatcher, block)
