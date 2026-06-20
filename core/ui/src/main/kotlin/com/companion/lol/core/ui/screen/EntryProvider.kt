package com.companion.lol.core.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.EntryProviderScope

inline fun <reified S : ScreenKey> entryProviderInstaller(
  crossinline content: @Composable (key: S) -> Unit
): EntryProviderInstaller = {
  entry<S>(
    metadata = { key: S -> ScreenMetadata.screenKey(key) + key.metadata },
    content = { key: S -> content(key) },
  )
}

typealias EntryProviderInstaller = EntryProviderScope<ScreenKey>.() -> Unit

@Immutable data class EntryProviderScopesCollector(val values: Set<EntryProviderInstaller>)
