package com.lol.app

import android.app.Application
import android.content.Context
import android.os.StrictMode
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.Logger
import com.companion.lol.app.BuildConfig
import com.lol.app.io.initializer.Initializers
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class CompanionLolApplication : Application(), SingletonImageLoader.Factory {

  @Inject lateinit var initializers: Initializers
  @Inject lateinit var coilImageLoader: ImageLoader

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      (Timber.plant(Timber.DebugTree()))
    }

    initializers.initialize()

    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().penaltyLog().build()
      )
      StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder()
          .detectActivityLeaks()
          .detectLeakedClosableObjects()
          .detectLeakedRegistrationObjects()
          .detectFileUriExposure()
          .detectCleartextNetwork()
          .detectContentUriWithoutPermission()
          .build()
      )
    }
  }

  override fun newImageLoader(context: Context): ImageLoader {
    return coilImageLoader
  }
}
