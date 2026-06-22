package com.companion.lol.ui.settings

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
object SettingsEntryModule : ScreenEntryModule {

  @IntoSet
  @Provides
  @Singleton
  override fun provideEntry() = screenEntry<SettingsKey> { SettingsScreen() }
}
