package com.companion.lol.app.di

import com.companion.lol.app.navigation.BackstackImpl
import com.companion.lol.app.navigation.NavigatorImpl
import com.companion.lol.app.ui.SnackBarManager
import com.companion.lol.app.ui.SnackBarManagerImpl
import com.companion.lol.core.ui.MessagePoster
import com.companion.lol.core.ui.navigation.BackStack
import com.companion.lol.core.ui.navigation.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
interface ActivityModule {

  @Binds @ActivityRetainedScoped fun messagePoster(impl: SnackBarManager): MessagePoster

  @Binds @ActivityRetainedScoped fun backStack(backStack: BackstackImpl): BackStack

  @Binds @ActivityRetainedScoped fun navigator(navigator: NavigatorImpl): Navigator

  @Binds @ActivityRetainedScoped fun snackBarManager(snackBar: SnackBarManagerImpl): SnackBarManager
}
