@file:OptIn(ExperimentalMaterial3Api::class)

package com.lol.app.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.navigation.serializer.ChampionIdSerializer
import com.lol.app.ui.screens.NavigationBarScreen
import kotlinx.serialization.Serializable

@Serializable
data object InitialScreenKey : ScreenKey {
  override val requiresAuth: Boolean = false
}

@Serializable
data object LoginKey : ScreenKey {
  override val requiresAuth: Boolean = false
}

@Serializable
data object ChampionListKey : ScreenKey, NavigationBarScreen {
  override val requiresAuth: Boolean = true
}

@Serializable
data class ChampionDetailsKey(
  @Serializable(ChampionIdSerializer::class) val championId: ChampionId
) : ScreenKey {

  override val screenType: ScreenKey.ScreenType = ScreenKey.ScreenType.BottomSheet()

  override val requiresAuth: Boolean = true
}

@Serializable
data object SettingsKey : ScreenKey, NavigationBarScreen {
  override val requiresAuth: Boolean = true
}
