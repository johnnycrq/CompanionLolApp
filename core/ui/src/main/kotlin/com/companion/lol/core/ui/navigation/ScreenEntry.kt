package com.companion.lol.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.EntryProviderScope
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

inline fun <reified S : ScreenKey> screenEntry(crossinline content: @Composable (key: S) -> Unit) =
  ScreenEntry(
    scope = {
      entry<S>(
        metadata = { key: S -> ScreenMetadata.screenKey(key) + key.metadata },
        content = { key: S -> content(key) },
      )
    },
    serializableModule =
      SerializersModule { polymorphic(ScreenKey::class) { subclass(S::class, serializer<S>()) } },
  )

data class ScreenEntry(
  val scope: EntryProviderScope<ScreenKey>.() -> Unit,
  val serializableModule: SerializersModule,
)

@Immutable
data class EntryProviderCollector(val values: Set<ScreenEntry>) :
  ScreenKeySerializerModule, ScreenEntryProviderScope {
  override val scopes: Set<EntryProviderScope<ScreenKey>.() -> Unit> =
    values.map { it.scope }.toSet()

  override val module: SerializersModule =
    values.map { it.serializableModule }.reduce { acc, module -> acc + module }
}
