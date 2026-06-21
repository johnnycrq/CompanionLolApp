package com.companion.lol.core.ui.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.EntryProviderScope

@Immutable
interface ScreenEntryProviderScope {
  val scopes: Set<EntryProviderScope<ScreenKey>.() -> Unit>
}
