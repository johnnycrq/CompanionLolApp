package com.companion.lol.app.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.navigation.keys.ScreenKey.Type
import com.companion.lol.app.util.withSnapshot

@Stable
interface BackStack<S : ScreenKey> {

  val history: List<S>

  val current: S

  fun goTo(key: S)

  fun setHistory(singleKey: S)

  fun setHistory(newHistory: List<S>)

  fun goBack(): Boolean

  companion object {
    @Suppress("AssignedValueIsNeverRead")
    inline fun <reified S : ScreenKey> SavedStateHandle.backStack(
      initialHistory: List<S>
    ): BackStack<S> {
      var saver: List<S> by this.saved { initialHistory }
      return Impl(initialValue = saver, saver = { saver = it })
    }
  }
}

class Impl<S : ScreenKey>(private val initialValue: List<S>, private val saver: (List<S>) -> Unit) :
  BackStack<S> {
  private val _history: SnapshotStateList<S> = SnapshotStateList<S>().apply { addAll(initialValue) }

  override val history: List<S>
    get() = _history

  override val current: S
    get() = _history.last()

  override fun setHistory(singleKey: S) {
    setHistory(listOf(singleKey))
    saver(history)
  }

  override fun setHistory(newHistory: List<S>) = withSnapshot {
    _history.clear()
    _history.addAll(newHistory)
    saver(history)
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
    saver(history)
  }

  override fun goBack(): Boolean {
    if (_history.size > 1) {
      _history.removeAt(_history.size - 1)
      saver(history)
      return true
    }
    return false
  }
}

interface BackStackSaver<S : ScreenKey> {
  fun get(): List<S>?

  fun save(newHistory: List<S>)
}
