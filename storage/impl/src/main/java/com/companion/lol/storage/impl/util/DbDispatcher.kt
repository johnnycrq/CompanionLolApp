package com.companion.lol.storage.impl.util

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable

@Singleton
class DbDispatcher(private val dispatcher: CoroutineDispatcher) : CoroutineDispatcher() {

  @Inject constructor() : this(Dispatchers.IO)

  override fun dispatch(context: CoroutineContext, block: Runnable) =
    dispatcher.dispatch(context, block)
}
