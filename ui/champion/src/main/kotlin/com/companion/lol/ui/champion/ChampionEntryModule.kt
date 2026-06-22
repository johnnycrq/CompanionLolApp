package com.companion.lol.ui.champion

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
object ChampionEntryModule : ScreenEntryModule {

  @IntoSet
  @Provides
  @Singleton
  override fun provideEntry() = screenEntry<ChampionKey> { ChampionScreen() }
}
