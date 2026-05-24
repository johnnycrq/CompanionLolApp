@file:Suppress("OPT_IN_TO_INHERITANCE")

package com.companion.lol.app.util

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

interface PersistedFlow<T> : StateFlow<T> {
  fun update(current: (T) -> T)
}

class MutablePersistedStateFlow<T>(
  private val flow: MutableStateFlow<T>,
  private val saver: (T) -> Unit,
) : PersistedFlow<T> {

  override val value: T
    get() = flow.value

  override val replayCache: List<T>
    get() = flow.replayCache

  override suspend fun collect(collector: FlowCollector<T>) = flow.collect(collector)

  override fun update(current: (T) -> T) =
    flow.update {
      saver(it)
      current(it)
    }
}

inline fun <reified T> SavedStateHandle.persistedFlow(initialValue: T): PersistedFlow<T> {
  val key = T::class.qualifiedName ?: T::class.java.name
  val serializer = serializer<T>()
  val initialValueAsRestored =
    this.get<String>(key)?.let { Json.decodeFromString(serializer, it) } ?: initialValue

  return MutablePersistedStateFlow(
    flow = MutableStateFlow(initialValueAsRestored),
    saver = { this@persistedFlow[key] = Json.encodeToString(serializer, it) },
  )
}
