@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.ui.details

import androidx.compose.material3.ExperimentalMaterial3Api
import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.ui.navigation.ScreenKey
import com.companion.lol.core.ui.navigation.ScreenMetadata
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ChampionDetailsKey(val championId: ChampionId) : ScreenKey {

  @Transient
  override val metadata: Map<String, Any> =
    ScreenMetadata.bottomSheet() + ScreenMetadata.topLevelDestination()

  override fun requiresAuth(): Boolean = true
}
