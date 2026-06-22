package com.companion.lol.app

import androidx.lifecycle.SavedStateHandle
import com.companion.lol.app.di.AppEntryModule
import com.companion.lol.app.di.IoModule
import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.ui.navigation.EntryProviderCollector
import com.companion.lol.core.ui.navigation.EntryScreenKey
import com.companion.lol.core.ui.navigation.ScreenKey
import com.companion.lol.core.ui.navigation.ScreenKeySaver
import com.companion.lol.ui.champion.ChampionKey
import com.companion.lol.ui.details.ChampionDetailsKey
import com.companion.lol.ui.login.LoginKey
import com.companion.lol.ui.settings.SettingsKey
import org.junit.Assert.assertEquals
import org.junit.Test

class ScreenKeySaverTest {
  private val collector =
    EntryProviderCollector(
      values =
        setOf(
          AppEntryModule.provideEntry(),
          com.companion.lol.ui.login.LoginEntryModule.provideEntry(),
          com.companion.lol.ui.settings.SettingsEntryModule.provideEntry(),
          com.companion.lol.ui.champion.ChampionEntryModule.provideEntry(),
          com.companion.lol.ui.details.ChampionDetailsEntryModule.provideEntry(),
        )
    )

  private val json = IoModule.json(collector)
  private val saver: () -> ScreenKeySaver = {
    ScreenKeySaver(module = collector.module, savedStateHandle = SavedStateHandle())
  }

  @Test
  fun testEmptyBackStack() {
    val backStack = emptyList<ScreenKey>()
    val saver = saver()

    saver.save(backStack)
    assertEquals(backStack, saver.restore())
  }

  @Test
  fun testSingleKey() {
    val backStack = listOf<ScreenKey>(EntryScreenKey)
    val saver = saver()
    saver.save(backStack)

    assertEquals(backStack, saver.restore())
  }

  @Test
  fun testMultipleKeys() {
    val backStack =
      listOf(
        EntryScreenKey,
        LoginKey,
        ChampionKey,
        ChampionDetailsKey(ChampionId(123)),
        SettingsKey,
      )
    val saver = saver()
    saver.save(backStack)

    assertEquals(backStack, saver.restore())
  }

  @Test
  fun testAllKeys() {
    val backStack =
      listOf(
        EntryScreenKey,
        LoginKey,
        SettingsKey,
        ChampionKey,
        ChampionDetailsKey(ChampionId(1)),
        ChampionDetailsKey(ChampionId(2)),
      )
    val saver = saver()
    saver.save(backStack)
    assertEquals(backStack, saver.restore())
  }
}
