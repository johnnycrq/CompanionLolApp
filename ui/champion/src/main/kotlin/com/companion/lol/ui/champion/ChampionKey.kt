package com.companion.lol.ui.champion

import com.companion.lol.core.ui.navigation.ScreenKey
import com.companion.lol.core.ui.navigation.ScreenMetadata
import kotlinx.serialization.Serializable

@Serializable
data object ChampionKey : ScreenKey {
  override fun requiresAuth(): Boolean = true

  override val metadata: Map<String, Any> = ScreenMetadata.topLevelDestination()
}
