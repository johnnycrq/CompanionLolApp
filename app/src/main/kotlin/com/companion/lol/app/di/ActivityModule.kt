package com.companion.lol.app.di

import com.companion.lol.app.navigation.BackstackImpl
import com.companion.lol.app.ui.SnackBarManager
import com.companion.lol.core.ui.MessagePoster
import com.companion.lol.core.ui.screen.BackStack
import com.companion.lol.core.ui.screen.InitialScreenKey
import com.companion.lol.core.ui.screen.ScreenKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityModule {

  @Binds @ActivityRetainedScoped abstract fun messagePoster(impl: SnackBarManager): MessagePoster

  companion object {
    @Provides
    @ActivityRetainedScoped
    fun backStack(): BackStack<ScreenKey> = BackstackImpl(listOf(InitialScreenKey))

    @Provides
    @ActivityRetainedScoped
    fun snackBarManagerImpl(): SnackBarManager = SnackBarManager.Impl()
  }
}
