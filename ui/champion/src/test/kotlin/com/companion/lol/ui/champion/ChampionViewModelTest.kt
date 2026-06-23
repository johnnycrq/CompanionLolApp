package com.companion.lol.ui.champion

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
    val container = ViewModelContainer()
    val state = container.viewModel.state.value

    assertEquals(emptyList<Champion>(), state.champions)
    assertEquals(GridSize.MEDIUM, state.gridSize)
    assertEquals(SortOrder.ASC, state.sortOrder)
  }

  @Test
  fun `state updates when champions and settings are observed`() = runTest {
    val container = ViewModelContainer()
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
    val champions = listOf(champion)

    container.observeChampion.emit(champions)
    container.observeSettings.emit(
      UserSettings(championGridSize = GridSize.LARGE, championSortOrder = SortOrder.FAVORITES)
    )
    advanceUntilIdle()

    assertEquals(GridSize.LARGE, container.viewModel.state.value.gridSize)
    assertEquals(SortOrder.FAVORITES, container.viewModel.state.value.sortOrder)
    assertEquals(1, container.viewModel.state.value.champions.size)
    assertEquals(listOf(champion), container.viewModel.state.value.champions)
  }

  @Test
  fun `init triggers refresh`() = runTest {
    val container = ViewModelContainer()
    container.observeChampion.emit(emptyList())
    advanceUntilIdle()

    assertEquals(1, container.refreshChampion.callCount)
  }

  @Test
  fun `onRefresh triggers user-initiated refresh`() = runTest {
    val container = ViewModelContainer()
    container.observeChampion.emit(emptyList())
    advanceUntilIdle()

    container.refreshChampion.callCount = 0
    container.viewModel.onRefresh()
    advanceUntilIdle()

    assertEquals(1, container.refreshChampion.callCount)
    assertEquals(true, container.refreshChampion.lastForceRefresh)
  }

  @Test
  fun `onRetry triggers refresh`() = runTest {
    val container = ViewModelContainer()
    container.observeChampion.emit(emptyList())
    advanceUntilIdle()

    container.refreshChampion.callCount = 0
    container.viewModel.onRetry()
    advanceUntilIdle()

    assertEquals(1, container.refreshChampion.callCount)
  }

  @Test
  fun `refresh failure sets error state`() = runTest {
    val container = ViewModelContainer()
    container.observeChampion.emit(emptyList())
    advanceUntilIdle()

    container.refreshChampion.callCount = 0
    container.refreshChampion.result = false
    container.viewModel.onRefresh()
    advanceUntilIdle()

    assertEquals(1, container.refreshChampion.callCount)
    assertEquals(true, container.viewModel.state.value.showError)
  }

  @Test
  fun `refresh success clears error state`() = runTest {
    val container = ViewModelContainer()
    container.observeChampion.emit(emptyList())
    advanceUntilIdle()

    container.refreshChampion.callCount = 0
    container.refreshChampion.result = true
    container.viewModel.onRefresh()
    advanceUntilIdle()

    assertEquals(1, container.refreshChampion.callCount)
    assertEquals(false, container.viewModel.state.value.showError)
  }

  @Test
  fun `changeGridSize calls updateSettings`() = runTest {
    val container = ViewModelContainer()
    container.observeChampion.emit(emptyList())
    advanceUntilIdle()

    container.viewModel.changeGridSize()
    advanceUntilIdle()

    assertEquals(1, container.updateSettings.callCount)
  }

  @Test
  fun `onSortMenuItemClicked calls updateSettings`() = runTest {
    val container = ViewModelContainer()
    container.observeChampion.emit(emptyList())
    advanceUntilIdle()

    container.viewModel.onSortMenuItemClicked()
    advanceUntilIdle()

    assertEquals(1, container.updateSettings.callCount)
  }

  @Test
  fun `onFavoritesClearClicked calls deleteFavorites`() = runTest {
    val container = ViewModelContainer()

    container.viewModel.onFavoritesClearClicked()
    advanceUntilIdle()

    assertEquals(1, container.deleteFavorites.callCount)
  }

  @Test
  fun `onCardClick navigates to champion details`() = runTest {
    val container = ViewModelContainer()
    val championId = ChampionId(42)

    container.viewModel.onCardClick(championId)

    assertEquals(1, container.navigator.navigateCount)
    assertEquals(championId, container.navigator.lastChampionId)
  }
}
