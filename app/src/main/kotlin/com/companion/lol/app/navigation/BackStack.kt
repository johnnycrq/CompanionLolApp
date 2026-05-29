package com.companion.lol.app.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.util.withSnapshot

@Stable
interface BackStack<S : ScreenKey> {

  val history: List<S>

  val current: S

  fun goTo(key: S)

  fun setHistory(singleKey: S)

  fun setHistory(newHistory: List<S>)

  fun goBack(): Boolean

  class Impl<S : ScreenKey>(private val initialValue: List<S>) : BackStack<S> {
    var save: ((List<S>) -> Unit) = {}
    private val _history: SnapshotStateList<S> =
      SnapshotStateList<S>().apply { addAll(initialValue) }
    override val history: List<S>
      get() = _history

    override val current: S
      get() = _history.last()

    override fun setHistory(singleKey: S) {
      setHistory(listOf(singleKey))
      save()
    }

    override fun setHistory(newHistory: List<S>) = withSnapshot {
      _history.clear()
      _history.addAll(newHistory)
      save()
    }

    override fun goTo(key: S) = withSnapshot {
      val last =
        _history.lastOrNull() ?: error("Cannot use goTo without having a valid non-empty history")

      // no repeated keys allowed
      if (last == key) return@withSnapshot

      val index = _history.indexOf(key)
      if (index != -1) {
        while (_history.size > index + 1) {
          _history.removeAt(_history.size - 1)
        }
      } else {
        _history.add(key)
      }
      save()
    }

    override fun goBack(): Boolean {
      if (_history.size > 1) {
        _history.removeAt(_history.size - 1)
        save()
        return true
      }
      return false
    }

    private fun save() {
      save(this.history.toList())
    }
  }
}
