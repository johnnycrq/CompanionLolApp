package com.companion.lol.core.ui.navigation

import androidx.compose.runtime.Stable
import kotlin.reflect.KClass

@Stable
interface ScreenKey {
  val metadata: Map<String, Any>
    get() = emptyMap()

  fun requiresAuth(): Boolean {
    return false
  }

  companion object {
    @Stable inline fun <reified S : ScreenKey> id(): String = id(S::class)

    @Stable fun <S : ScreenKey> id(instance: KClass<S>): String = instance.simpleName!!
  }
}
