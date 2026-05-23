package com.lol.app.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.lol.app.navigation.keys.ScreenKey
import com.lol.app.navigation.keys.ScreenKey.Type
import com.lol.app.util.withSnapshot
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

private const val SAVED_KEY = "BackStack.KEY"

@Stable
interface BackStack<S : ScreenKey> {
  val history: List<S>

  val current: S

  fun goTo(key: S)

  fun setHistory(singleKey: S)

  fun setHistory(newHistory: List<S>)

  fun goBack(): Boolean

  companion object {
    inline fun <reified S : ScreenKey> SavedStateHandle.backStack(
      initialHistory: List<S>
    ): BackStack<S> {
      val saver = BackStackSaver.Impl(this, serializer<S>())
      return Impl(saver = saver, initialHistory = saver.get() ?: initialHistory)
    }
  }
}

class Impl<S : ScreenKey>(
  private val saver: BackStackSaver<S>,
  private val initialHistory: List<S>,
) : BackStack<S> {
  private val _history: SnapshotStateList<S> =
    SnapshotStateList<S>().apply { addAll(initialHistory) }

  override val history: List<S>
    get() = _history

  override val current: S
    get() = _history.last()

  override fun setHistory(singleKey: S) {
    setHistory(listOf(singleKey))
    saver.save(history)
  }

  override fun setHistory(newHistory: List<S>) = withSnapshot {
    _history.clear()
    _history.addAll(newHistory)
    saver.save(history)
  }

  override fun goTo(key: S) = withSnapshot {
    val last =
      _history.lastOrNull() ?: error("Cannot use goTo without having a valid non-empty history")

    // no repeated keys allowed
    if (last == key) return@withSnapshot

    // if last key is a bottomSheet, cannot go to any other screen
    if (last.type() is Type.BottomSheet) {
      return@withSnapshot
    }

    val index = _history.indexOf(key)
    if (index != -1) {
      while (_history.size > index + 1) {
        _history.removeAt(_history.size - 1)
      }
    } else {
      _history.add(key)
    }
    saver.save(history)
  }

  override fun goBack(): Boolean {
    if (_history.size > 1) {
      _history.removeAt(_history.size - 1)
      saver.save(history)
      return true
    }
    return false
  }
}

interface BackStackSaver<S : ScreenKey> {
  fun get(): List<S>?

  fun save(newHistory: List<S>)

  class Impl<S : ScreenKey>(
    private val savedStateHandle: SavedStateHandle,
    private val serializer: KSerializer<S>,
  ) : BackStackSaver<S> {
    override fun get(): List<S>? {
      return savedStateHandle.get<String>(SAVED_KEY)?.let {
        Json.decodeFromString(ListSerializer(serializer), it)
      }
    }

    override fun save(newHistory: List<S>) {
      savedStateHandle[SAVED_KEY] = Json.encodeToString(ListSerializer(serializer), newHistory)
    }
  }
}
