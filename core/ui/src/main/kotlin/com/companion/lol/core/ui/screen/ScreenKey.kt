package com.companion.lol.core.ui.screen

import androidx.compose.runtime.Stable
import kotlin.reflect.KClass
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed interface ScreenKey {
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
