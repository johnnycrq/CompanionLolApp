package com.lol.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

@Stable
interface BackStack<S : ScreenKey> {
  val history: List<S>

  val current: S

  fun goTo(key: S)

  fun setHistory(singleKey: S)

  fun setHistory(newHistory: List<S>)

  fun goBack(): Boolean
}

@Serializable
private class Impl<S : ScreenKey>(private val initialHistory: List<S>) : BackStack<S> {

  @Serializable(SnapshotStateListSerializable::class)
  private val _history: SnapshotStateList<S> =
    SnapshotStateList<S>().apply { addAll(initialHistory) }

  override val history: List<S>
    get() = _history

  override val current: S
    get() = _history.last()

  override fun setHistory(singleKey: S) = setHistory(listOf(singleKey))

  override fun setHistory(newHistory: List<S>) {
    Snapshot.withMutableSnapshot {
      _history.clear()
      _history.addAll(newHistory)
    }
  }

  override fun goTo(key: S) {
    Snapshot.withMutableSnapshot {
      if (_history.lastOrNull() == key) return@withMutableSnapshot

      val index = _history.indexOf(key)
      if (index != -1) {
        while (_history.size > index + 1) {
          _history.removeAt(_history.size - 1)
        }
      } else {
        _history.add(key)
      }
    }
  }

  override fun goBack(): Boolean {
    if (_history.size > 1) {
      _history.removeAt(_history.size - 1)
      return true
    }
    return false
  }
}

@Composable
fun rememberCompanionAppBackstack(elements: List<ScreenKey>): BackStack<ScreenKey> =
  rememberSerializable(serializer = serializer()) {
    Impl(elements)
  }

private class SnapshotStateListSerializable<S : ScreenKey>(
  elementSerializer: KSerializer<S>
) : KSerializer<SnapshotStateList<S>> {
  private val delegate = ListSerializer(elementSerializer)

  override val descriptor: SerialDescriptor = delegate.descriptor

  override fun serialize(encoder: Encoder, value: SnapshotStateList<S>) {
    encoder.encodeSerializableValue(delegate, value.toList())
  }

  override fun deserialize(decoder: Decoder): SnapshotStateList<S> {
    val list = decoder.decodeSerializableValue(delegate)
    return SnapshotStateList<S>().apply { addAll(list) }
  }
}
