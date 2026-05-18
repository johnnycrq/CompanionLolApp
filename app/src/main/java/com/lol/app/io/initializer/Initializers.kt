package com.lol.app.io.initializer

import com.companion.lol.data.usecase.RefreshChampionsUseCase
import com.lol.app.io.AppScope
import dagger.Lazy
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface Initializers{
    fun initialize()
}

@Singleton
internal class InitializersImpl @Inject constructor(
    private val refreshChampions: Lazy<RefreshChampionsUseCase>,
    private val appScope: AppScope
): Initializers {
    @OptIn(DelicateCoroutinesApi::class)
    override fun initialize() {
        appScope.launch {
            /*
            Okhttp have some initialization on main thread that trigger StrictMode.
            Since the dependencies are initialized on first usage, and its here,
            we prewarm it in IO dispatcher so it doesn't block the main thread
             */
            withContext(Dispatchers.IO) { refreshChampions.get() }
            refreshChampions.get().refresh()
        }
    }
}