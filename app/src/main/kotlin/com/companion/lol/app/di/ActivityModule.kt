package com.companion.lol.app.di

import com.companion.lol.app.navigation.BackstackImpl
import com.companion.lol.app.navigation.NavigatorImpl
import com.companion.lol.app.ui.SnackBarManager
import com.companion.lol.core.ui.MessagePoster
import com.companion.lol.core.ui.navigation.BackStack
import com.companion.lol.core.ui.navigation.InitialScreenKey
import com.companion.lol.core.ui.navigation.Navigator
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

  @Binds @ActivityRetainedScoped abstract fun backStack(backStack: BackstackImpl): BackStack

  companion object {
    @Provides @ActivityRetainedScoped fun backStack() = BackstackImpl(listOf(InitialScreenKey))

    @Provides
    @ActivityRetainedScoped
    fun navigator(backStack: BackStack): Navigator = NavigatorImpl(backStack)

    @Provides
    @ActivityRetainedScoped
    fun snackBarManagerImpl(): SnackBarManager = SnackBarManager.Impl()
  }
}
