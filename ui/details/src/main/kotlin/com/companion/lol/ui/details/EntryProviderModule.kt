package com.companion.lol.ui.details

import com.companion.lol.core.ui.screen.ChampionDetailsKey
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
    entryProviderInstaller<ChampionDetailsKey> { key ->
      ChampionDetailsScreen(championId = key.championId)
    }
}
