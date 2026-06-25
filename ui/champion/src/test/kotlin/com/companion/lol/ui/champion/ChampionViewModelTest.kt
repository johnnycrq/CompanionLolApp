package com.companion.lol.ui.champion

import app.cash.turbine.test
import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.model.DdragonImage
import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.PartyType
import com.companion.lol.core.model.SortOrder
import com.companion.lol.domain.model.Champion
import com.companion.lol.domain.model.UserSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChampionViewModelTest {

  class ViewModelContainer(
    val refreshChampion: RefreshChampionFake = RefreshChampionFake(),
    val updateSettings: UpdateSettingsFake = UpdateSettingsFake(),
    val deleteFavorites: DeleteFavoritesFake = DeleteFavoritesFake(),
    val observeChampion: ObserveChampionFake = ObserveChampionFake(),
    val navigator: NavigatorFake = NavigatorFake(),
    val observeSettings: ObserveSettingsFake = ObserveSettingsFake(),
  ) {
    val viewModel =
      ChampionViewModel(
        refreshChampions = refreshChampion,
        updateSettings = updateSettings,
        deleteFavorites = deleteFavorites,
        observeChampions = observeChampion,
        navigator = navigator,
        observeSession = observeSettings,
      )
  }

  private fun viewModelContainer() = ViewModelContainer()

  @Before
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `initial state has default values`() = runTest {
    viewModelContainer().viewModel.state.test {
      val state = awaitItem()
      assertEquals(emptyList<Champion>(), state.champions)
      assertEquals(GridSize.MEDIUM, state.gridSize)
      assertEquals(SortOrder.ASC, state.sortOrder)
    }
  }

  @Test
  fun `state updates when champions and settings are observed`() = runTest {
    val container = viewModelContainer()

    container.viewModel.state.test {
      val champion =
        Champion(
          id = ChampionId(1),
          name = "Ahri",
          keyName = "Ahri".lowercase(),
          title = "The Ahri",
          squareImage = DdragonImage.Square("https://example.com/Ahri.png"),
          partyType = PartyType.MANA,
          isFavorite = false,
        )

      container.observeChampion.emit(listOf(champion))
      container.observeSettings.emit(
        UserSettings(championGridSize = GridSize.LARGE, championSortOrder = SortOrder.FAVORITES)
      )
      advanceUntilIdle()

      val state = expectMostRecentItem()
      assertEquals(GridSize.LARGE, state.gridSize)
      assertEquals(SortOrder.FAVORITES, state.sortOrder)
      assertEquals(listOf(champion), state.champions)
    }
  }

  @Test
  fun `refresh failure sets error state`() = runTest {
    val container = viewModelContainer()

    container.viewModel.state.test {
      container.observeChampion.emit(emptyList())
      advanceUntilIdle()

      container.refreshChampion.result = false
      container.viewModel.onRefresh()
      advanceUntilIdle()

      assertEquals(true, expectMostRecentItem().showError)
    }
  }

  @Test
  fun `refresh success clears error state`() = runTest {
    val container = viewModelContainer()

    container.viewModel.state.test {
      container.observeChampion.emit(emptyList())
      advanceUntilIdle()

      container.refreshChampion.result = true
      container.viewModel.onRefresh()
      advanceUntilIdle()

      assertEquals(false, expectMostRecentItem().showError)
    }
  }
}
