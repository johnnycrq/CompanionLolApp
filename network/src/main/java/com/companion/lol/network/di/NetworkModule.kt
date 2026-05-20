package com.companion.lol.network.di

import com.companion.lol.network.BuildConfig
import com.companion.lol.network.DDragonApi
import com.companion.lol.network.EndPoints
import com.companion.lol.network.interceptors.RiotDevPortalApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @OptIn(ExperimentalSerializationApi::class)
  @Provides
  @Singleton
  internal fun baseRetrofit(): Retrofit {
    // This is an ugly hack
    // But StrictMode identifies violation here
    // somewhere in the initialization
    @Suppress("JSON_FORMAT_REDUNDANT")
    return runBlocking(Dispatchers.IO) {
      Retrofit.Builder()
        .addConverterFactory(
          Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType())
        )
        .baseUrl("http://www.google.com") // we replace this later
        .build()
    }
  }

  @Provides
  @Singleton
  internal fun httpInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }
  }

  @Provides
  @Singleton
  internal fun baseOkhttpClient(
    logInterceptor: HttpLoggingInterceptor,
    apiKeyInterceptor: RiotDevPortalApiKeyInterceptor,
  ): OkHttpClient =
    OkHttpClient.Builder()
      .apply {
        if (BuildConfig.DEBUG) {
          addInterceptor(logInterceptor)
        }
        addInterceptor(apiKeyInterceptor)
      }
      .build()

  @Provides
  @Singleton
  internal fun dDragonApi(retrofit: Retrofit, okhttpClient: OkHttpClient): DDragonApi {
    val publicRetrofit =
      retrofit.newBuilder().baseUrl(EndPoints.DDragon.base).client(okhttpClient).build()

    return publicRetrofit.create(DDragonApi::class.java)
  }
}
