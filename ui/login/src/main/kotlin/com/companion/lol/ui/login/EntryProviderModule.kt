package com.companion.lol.ui.login

import com.companion.lol.core.ui.screen.LoginKey
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
  fun provideEntryProviderInstaller() = entryProviderInstaller<LoginKey> { LoginScreen() }
}
