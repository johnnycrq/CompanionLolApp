package com.companion.lol.ui.details

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
object ChampionDetailsEntryModule : ScreenEntryModule {

  @IntoSet
  @Provides
  @Singleton
  override fun provideEntry() =
    screenEntry<ChampionDetailsKey> { key -> ChampionDetailsScreen(championId = key.championId) }
}
