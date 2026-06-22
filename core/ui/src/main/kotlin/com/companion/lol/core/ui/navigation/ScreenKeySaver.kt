package com.companion.lol.core.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

private const val SAVER_KEY = "backstack_saver"

interface ScreenKeySerializerModule {
  val module: SerializersModule
}

interface ScreenKeySaver {
  fun save(history: List<ScreenKey>)

  fun restore(): List<ScreenKey>?
}

fun ScreenKeySaver(module: SerializersModule, savedStateHandle: SavedStateHandle): ScreenKeySaver =
  Impl(
    savedStateHandle = savedStateHandle,
    serializer = module.serializer<List<ScreenKey>>().nullable,
    config = SavedStateConfiguration { serializersModule = module },
  )

private class Impl(
  savedStateHandle: SavedStateHandle,
  serializer: KSerializer<List<ScreenKey>?>,
  config: SavedStateConfiguration,
) : ScreenKeySaver {
  private var savedStateSaver by
    savedStateHandle.saved(key = SAVER_KEY, serializer = serializer, configuration = config) {
      null
    }

  override fun save(history: List<ScreenKey>) {
    this.savedStateSaver = history
  }

  override fun restore(): List<ScreenKey>? {
    return savedStateSaver
  }
}
