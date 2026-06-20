package com.companion.lol.app.di

import android.app.Application
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.companion.lol.app.BuildConfig
import com.companion.lol.app.io.worker.SyncWorker
import com.companion.lol.core.io.AppDispatchers
import com.companion.lol.core.io.SyncWorkerDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object IoModule {

  @Provides
  @Singleton
  internal fun coilImageLoader(okhttpClient: OkHttpClient, context: Application): ImageLoader =
    ImageLoader.Builder(context)
      .components { add(OkHttpNetworkFetcherFactory(callFactory = { okhttpClient })) }
      .logger(if (BuildConfig.DEBUG) DebugLogger() else null)
      .crossfade(true)
      .build()

  @Provides
  @Singleton
  internal fun appDispatchers(): AppDispatchers =
    AppDispatchers(
      io = Dispatchers.IO,
      computation = Dispatchers.Default,
      main = Dispatchers.Main.immediate,
    )

  @Provides
  @Singleton
  internal fun syncWorkerDispatcher(): SyncWorkerDispatcher = SyncWorker.Companion
}
