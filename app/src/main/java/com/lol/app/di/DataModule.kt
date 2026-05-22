package com.lol.app.di

import android.app.Application
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.lol.app.io.AppScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

  @Provides
  @Singleton
  internal fun coilImageLoader(okhttpClient: OkHttpClient, context: Application): ImageLoader =
    ImageLoader.Builder(context)
      .components { add(OkHttpNetworkFetcherFactory(callFactory = { okhttpClient })) }
      .logger(DebugLogger())
      .crossfade(true)
      .build()

  @Provides
  @Singleton
  internal fun appScope(): AppScope =
    object : AppScope {
      private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

      override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext
    }
}
