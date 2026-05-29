package com.companion.lol.app.di

import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.keys.InitialScreenKey
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.ui.MessagePoster
import com.companion.lol.app.ui.SnackBarManager
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
    fun backStack(): BackStack<ScreenKey> = BackStack.Impl(listOf(InitialScreenKey))

    @Provides
    @ActivityRetainedScoped
    fun snackBarManagerImpl(): SnackBarManager = SnackBarManager.Impl()
  }
}
