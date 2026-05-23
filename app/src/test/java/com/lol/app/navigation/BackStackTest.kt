package com.lol.app.navigation

import androidx.lifecycle.SavedStateHandle
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.navigation.BackStack.Companion.backStack
import com.lol.app.navigation.keys.ChampionDetailsKey
import com.lol.app.navigation.keys.LoginKey
import com.lol.app.navigation.keys.ScreenKey
import com.lol.app.navigation.keys.SettingsKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackStackTest {
  private val saver =
    object : BackStackSaver<ScreenKey> {
      var savedHistory: List<ScreenKey>? = null

      override fun get(): List<ScreenKey>? = savedHistory

      override fun save(newHistory: List<ScreenKey>) {
        savedHistory = newHistory
      }
    }

  private val screenA = InitialScreenKey
  private val screenB = LoginKey
  private val screenC = SettingsKey
  private val bottomSheetScreen = ChampionDetailsKey(ChampionId(1))
  private val createBackStack = { history: List<ScreenKey> ->
    Impl(saver = saver, initialHistory = history)
  }

  @Test
  fun `initial history is set correctly`() {
    val backStack = createBackStack(listOf(screenA))

    assertEquals(listOf(screenA), backStack.history)
    assertEquals(screenA, backStack.current)
  }

  @Test
  fun `setHistory single key clears and sets new key`() {
    val backStack = createBackStack(listOf(screenA))

    backStack.setHistory(screenB)

    assertEquals(listOf(screenB), backStack.history)
    assertEquals(listOf(screenB), saver.savedHistory)
  }

  @Test
  fun `setHistory multiple keys clears and sets new history`() {
    val backStack = createBackStack(listOf(screenA))

    backStack.setHistory(listOf(screenB, screenC))

    assertEquals(listOf(screenB, screenC), backStack.history)
    assertEquals(listOf(screenB, screenC), saver.savedHistory)
  }

  @Test
  fun `goTo adds new key to history`() {
    val backStack = createBackStack(listOf(screenA))

    backStack.goTo(screenB)

    assertEquals(listOf(screenA, screenB), backStack.history)
    assertEquals(listOf(screenA, screenB), saver.savedHistory)
  }

  @Test
  fun `goTo same key as current is no-op`() {
    val backStack = createBackStack(listOf(screenA))

    backStack.goTo(screenA)

    assertEquals(listOf(screenA), backStack.history)
  }

  @Test
  fun `goTo existing key in history pops until that key`() {
    val backStack = createBackStack(listOf(screenA, screenB, screenC))

    backStack.goTo(screenA)

    assertEquals(listOf(screenA), backStack.history)
    assertEquals(listOf(screenA), saver.savedHistory)
  }

  @Test
  fun `goTo from BottomSheet is no-op`() {
    val backStack = createBackStack(listOf(screenA, bottomSheetScreen))

    backStack.goTo(screenB)

    assertEquals(listOf(screenA, bottomSheetScreen), backStack.history)
  }

  @Test
  fun `goBack removes last item and returns true`() {
    val backStack = createBackStack(listOf(screenA, screenB))

    val result = backStack.goBack()

    assertTrue(result)
    assertEquals(listOf(screenA), backStack.history)
    assertEquals(listOf(screenA), saver.savedHistory)
  }

  @Test
  fun `goBack on single item returns false and does nothing`() {
    val backStack = createBackStack(listOf(screenA))

    val result = backStack.goBack()

    assertFalse(result)
    assertEquals(listOf(screenA), backStack.history)
  }

  @Test
  fun `SavedStateHandle backStack extension initializes correctly`() {
    val savedStateHandle = SavedStateHandle()
    val backStack = savedStateHandle.backStack<ScreenKey>(listOf(screenA))

    assertEquals(listOf(screenA), backStack.history)
  }

  @Test
  fun `SavedStateHandle backStack extension restores history`() {
    val savedStateHandle = SavedStateHandle()
    // Manually save to SSH as BackStackSaver.Impl does
    val initialBackStack = savedStateHandle.backStack<ScreenKey>(listOf(screenA))
    initialBackStack.goTo(screenB)

    // New instance with same SSH should restore
    val restoredBackStack = savedStateHandle.backStack<ScreenKey>(listOf(screenA))

    assertEquals(listOf(screenA, screenB), restoredBackStack.history)
  }
}
