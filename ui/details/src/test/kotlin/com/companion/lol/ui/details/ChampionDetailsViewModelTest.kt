package com.companion.lol.ui.details

import app.cash.turbine.test
import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.model.ChampionTag
import com.companion.lol.core.model.DdragonImage
import com.companion.lol.core.model.PartyType
import com.companion.lol.domain.model.Champion
import com.companion.lol.domain.model.ChampionDetails
import com.companion.lol.domain.model.ChampionWithDetails
import com.companion.lol.domain.usecase.RefreshChampionDetailsFake
import com.companion.lol.domain.usecase.UpdateFavoritesFake
import com.companion.lol.domain.usecase.impl.ObserveChampionDetailsFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChampionDetailsViewModelTest {

  private val championId = ChampionId(1)

  class ViewModelContainer(
    championId: ChampionId,
    val observeChampionDetails: ObserveChampionDetailsFake = ObserveChampionDetailsFake(),
    val refreshChampionDetails: RefreshChampionDetailsFake = RefreshChampionDetailsFake(),
    val updateFavorites: UpdateFavoritesFake = UpdateFavoritesFake(),
    val messagePoster: MessagePosterFake = MessagePosterFake(),
    val backStack: BackStackFake = BackStackFake(),
  ) {
    val viewModel =
      ChampionDetailsViewModel(
        championId = championId,
        messagePoster = messagePoster,
        backStack = backStack,
        refreshChampionDetails = refreshChampionDetails,
        observeChampionDetails = observeChampionDetails,
        updateFavorites = updateFavorites,
      )
  }

  private fun viewModelContainer(refreshResult: Boolean = true): ViewModelContainer {
    val container = ViewModelContainer(championId = championId)
    container.refreshChampionDetails.result = refreshResult
    return container
  }

  @Before
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `initial state has championId and no data`() = runTest {
    val container = viewModelContainer()

    container.viewModel.state.test {
      val state = awaitItem()
      assertEquals(championId, state.championId)
      assertNull(state.champion)
      assertNull(state.details)
      assertFalse(state.hasData)
    }
  }

  @Test
  fun `state updates when champion details are observed`() = runTest {
    val container = viewModelContainer()

    container.viewModel.state.test {
      skipItems(1) // skip initial state

      val championWithDetails = createChampionWithDetails()
      container.observeChampionDetails.emit(championWithDetails)
      advanceUntilIdle()

      val state = expectMostRecentItem()
      assertEquals(championId, state.championId)
      assertEquals(championWithDetails.champion, state.champion)
      assertEquals(championWithDetails.details, state.details)
      assertTrue(state.hasData)
    }
  }

  @Test
  fun `refresh is called on init`() = runTest {
    val container = viewModelContainer()
    advanceUntilIdle()

    assertEquals(1, container.refreshChampionDetails.callCount)
  }

  @Test
  fun `when refresh fails and no data then posts error and goes back`() = runTest {
    val container = viewModelContainer(refreshResult = false)
    advanceUntilIdle()

    assertEquals(1, container.messagePoster.callCount)
    assertEquals(1, container.backStack.goBackCount)
  }

  @Test
  fun `when refresh fails but has data then does not go back`() = runTest {
    val container = viewModelContainer(refreshResult = false)

    // emit data before refresh completes
    container.observeChampionDetails.emit(createChampionWithDetails())
    advanceUntilIdle()

    assertEquals(0, container.messagePoster.callCount)
    assertEquals(0, container.backStack.goBackCount)
  }

  @Test
  fun `onFavoritesClicked toggles favorite`() = runTest {
    val container = viewModelContainer()

    container.observeChampionDetails.emit(createChampionWithDetails(isFavorite = false))
    advanceUntilIdle()

    container.viewModel.onFavoritesClicked()
    advanceUntilIdle()

    assertEquals(1, container.updateFavorites.callCount)
    assertEquals(championId, container.updateFavorites.lastChampionId)
    assertEquals(true, container.updateFavorites.lastIsFavorite)
  }

  @Test
  fun `onFavoritesClicked does nothing when champion is null`() = runTest {
    val container = viewModelContainer()
    advanceUntilIdle()

    container.viewModel.onFavoritesClicked()
    advanceUntilIdle()

    assertEquals(0, container.updateFavorites.callCount)
  }

  private fun createChampionWithDetails(isFavorite: Boolean = false) =
    ChampionWithDetails(
      champion =
        Champion(
          id = championId,
          name = "Ahri",
          keyName = "Ahri",
          title = "the Nine-Tailed Fox",
          squareImage = DdragonImage.Square(imageUrl = "https://example.com/ahri.png"),
          partyType = PartyType.MANA,
          isFavorite = isFavorite,
        ),
      details =
        ChampionDetails(
          lore = "Ahri lore",
          blurb = "Ahri blurb",
          tags = listOf(ChampionTag.MAGE, ChampionTag.ASSASSIN),
          skins = emptyList(),
        ),
    )
}
