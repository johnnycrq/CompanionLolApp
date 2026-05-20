package com.companion.lol.network.interceptors

import com.companion.lol.network.ApiKeys
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
internal class RiotDevPortalApiKeyInterceptor @Inject constructor(private val apiKeys: ApiKeys) :
  Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    return chain.proceed(
      chain.request().newBuilder().addHeader("X-Riot-Token", apiKeys.riotDevPortalApiKey).build()
    )
  }
}
