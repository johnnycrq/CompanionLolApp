@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.navigation3.runtime.get
import com.companion.lol.app.util.withSnapshot
import com.companion.lol.core.ui.navigation.BackStack
import com.companion.lol.core.ui.navigation.ScreenKey
import com.companion.lol.core.ui.navigation.ScreenKeySaver
import com.companion.lol.core.ui.navigation.ScreenKeySerializerModule
import com.companion.lol.core.ui.navigation.ScreenMetadata

class BackstackImpl(private val initialValue: List<ScreenKey>) : BackStack {
  override val history = SnapshotStateList<ScreenKey>().apply { addAll(initialValue) }
  override val current: ScreenKey
    get() = history.last()

  override fun setHistory(singleKey: ScreenKey) =
    withSnapshot {
        history.clear()
        history.add(singleKey)
      }
      .also { save() }

  override fun setHistory(newHistory: List<ScreenKey>) =
    withSnapshot {
        history.clear()
        history.addAll(newHistory)
      }
      .also { save() }

  override fun goTo(key: ScreenKey) =
    withSnapshot {
        val last =
          history.lastOrNull() ?: error("Cannot use goTo without having a valid non-empty history")

        // no repeated keys allowed
        if (last == key) return@withSnapshot

        // cant navigate from bottom sheet
        if (last.metadata[ScreenMetadata.BottomSheet] != null) {
          return@withSnapshot
        }

        val index = history.indexOf(key)
        if (index != -1) {
          while (history.size > index + 1) {
            history.removeAt(history.size - 1)
          }
        } else {
          history.add(key)
        }
      }
      .also { save() }

  override fun goBack(): Boolean {
    if (history.size > 1) {
      history.removeAt(history.size - 1)
      save()
      return true
    }
    return false
  }

  private var saver: ScreenKeySaver? = null

  fun attachSaver(
    savedStateHandle: SavedStateHandle,
    serializerModule: ScreenKeySerializerModule,
    restore: Boolean,
  ) {
    this.saver = serializerModule.ScreenKeySaver(savedStateHandle)

    if (restore) {
      restore()
    }
  }

  private fun restore() {
    saver?.restore()?.let(::setHistory)
  }

  private fun save() {
    saver?.save(history.toList())
  }
}
