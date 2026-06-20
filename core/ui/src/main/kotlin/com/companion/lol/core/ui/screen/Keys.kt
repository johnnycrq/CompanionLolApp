@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.core.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import com.companion.lol.core.model.ChampionId
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable data object InitialScreenKey : ScreenKey

@Serializable data object LoginKey : ScreenKey

@Serializable
data object ChampionKey : ScreenKey {
  override fun requiresAuth(): Boolean = true

  override val metadata: Map<String, Any> = ScreenMetadata.topLevelDestination()
}

@Serializable
data class ChampionDetailsKey(private val id: Int) : ScreenKey {

  constructor(championId: ChampionId) : this(championId.value)

  @Transient val championId: ChampionId = ChampionId(this.id)

  @Transient
  override val metadata: Map<String, Any> =
    ScreenMetadata.bottomSheet() + ScreenMetadata.topLevelDestination()

  override fun requiresAuth(): Boolean = true
}

@Serializable
data object SettingsKey : ScreenKey {
  override fun requiresAuth(): Boolean = true

  override val metadata: Map<String, Any> = ScreenMetadata.topLevelDestination()
}
