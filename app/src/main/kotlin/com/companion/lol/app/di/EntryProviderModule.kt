package com.companion.lol.app.di

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import com.companion.lol.core.ui.screen.EntryProviderInstaller
import com.companion.lol.core.ui.screen.EntryProviderScopesCollector
import com.companion.lol.core.ui.screen.InitialScreenKey
import com.companion.lol.core.ui.screen.entryProviderInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EntryProviderModule {
  @IntoSet
  @Provides
  @Singleton
  fun provideEntryProviderInstaller() =
    entryProviderInstaller<InitialScreenKey> { Box(modifier = Modifier) }

  @Provides
  @Singleton
  fun entryScopes(values: Set<@JvmSuppressWildcards EntryProviderInstaller>) =
    EntryProviderScopesCollector(values)
}
