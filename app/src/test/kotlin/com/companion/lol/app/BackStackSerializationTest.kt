package com.companion.lol.app

import com.companion.lol.app.di.EntryProviderModule
import com.companion.lol.app.di.IoModule
import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.ui.navigation.EntryProviderCollector
import com.companion.lol.core.ui.navigation.InitialScreenKey
import com.companion.lol.core.ui.navigation.ScreenKey
import com.companion.lol.ui.champion.ChampionKey
import com.companion.lol.ui.details.ChampionDetailsKey
import com.companion.lol.ui.login.LoginKey
import com.companion.lol.ui.settings.SettingsKey
import org.junit.Assert.assertEquals
import org.junit.Test

class BackStackSerializationTest {
  private val collector =
    EntryProviderCollector(
      values =
        setOf(
          EntryProviderModule.provideEntryProviderInstaller(),
          com.companion.lol.ui.login.EntryProviderModule.provideEntryProviderInstaller(),
          com.companion.lol.ui.settings.EntryProviderModule.provideEntryProviderInstaller(),
          com.companion.lol.ui.champion.EntryProviderModule.provideEntryProviderInstaller(),
          com.companion.lol.ui.details.EntryProviderModule.provideEntryProviderInstaller(),
        )
    )

  private val json = IoModule.json(collector)

  @Test
  fun testEmptyBackStack() {
    val backStack = emptyList<ScreenKey>()
    val serialized = json.encodeToString(backStack)
    val deserialized = json.decodeFromString<List<ScreenKey>>(serialized)
    assertEquals(backStack, deserialized)
  }

  @Test
  fun testSingleKey() {
    val backStack = listOf<ScreenKey>(InitialScreenKey)
    val serialized = json.encodeToString(backStack)
    val deserialized = json.decodeFromString<List<ScreenKey>>(serialized)
    assertEquals(backStack, deserialized)
  }

  @Test
  fun testMultipleKeys() {
    val backStack =
      listOf(
        InitialScreenKey,
        LoginKey,
        ChampionKey,
        ChampionDetailsKey(ChampionId(123)),
        SettingsKey,
      )
    val serialized: String = json.encodeToString(backStack)
    val deserialized: List<ScreenKey> = json.decodeFromString<List<ScreenKey>>(serialized)
    assertEquals(backStack, deserialized)
  }

  @Test
  fun testAllKeys() {
    val backStack =
      listOf(
        InitialScreenKey,
        LoginKey,
        SettingsKey,
        ChampionKey,
        ChampionDetailsKey(ChampionId(1)),
        ChampionDetailsKey(ChampionId(2)),
      )
    val serialized = json.encodeToString(backStack)
    val deserialized = json.decodeFromString<List<ScreenKey>>(serialized)
    assertEquals(backStack, deserialized)
  }
}
