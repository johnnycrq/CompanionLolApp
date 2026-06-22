package com.companion.lol.storage.impl.util

import com.companion.lol.core.io.AppDispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

interface DatabaseContext : CoroutineContext

@Singleton
class DatabaseContextImpl @Inject constructor(private val dispatchers: AppDispatchers) :
  DatabaseContext, CoroutineContext by dispatchers.io
