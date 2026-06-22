package com.companion.lol.app.di

import com.companion.lol.core.ui.navigation.EntryProviderCollector
import com.companion.lol.core.ui.navigation.EntryScreenKey
import com.companion.lol.core.ui.navigation.ScreenEntry
import com.companion.lol.core.ui.navigation.ScreenEntryModule
import com.companion.lol.core.ui.navigation.screenEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppEntryModule : ScreenEntryModule {
  @IntoSet @Provides @Singleton override fun provideEntry() = screenEntry<EntryScreenKey> {}

  @Provides
  @Singleton
  fun entryScopes(values: Set<@JvmSuppressWildcards ScreenEntry>) = EntryProviderCollector(values)
}
