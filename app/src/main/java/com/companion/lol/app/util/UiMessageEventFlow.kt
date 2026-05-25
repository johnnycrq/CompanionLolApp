package com.companion.lol.app.util

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

@Stable
interface UiMessageEventFlow<T> : Flow<T> {
  class Impl<T> : UiMessageEventFlow<T> {
    private val flow = MutableSharedFlow<T>(extraBufferCapacity = 1)

    suspend fun emit(event: T) {
      this.flow.emit(event)
    }

    fun tryEmit(event: T) {
      this.flow.tryEmit(event)
    }

    override suspend fun collect(collector: FlowCollector<T>) {
      flow.collect(collector)
    }
  }
}
