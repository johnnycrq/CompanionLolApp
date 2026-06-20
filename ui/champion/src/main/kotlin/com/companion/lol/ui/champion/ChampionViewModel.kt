@file:Suppress("CanBeParameter")

package com.companion.lol.ui.champion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.ui.screen.BackStack
import com.companion.lol.core.ui.screen.ChampionDetailsKey
import com.companion.lol.core.ui.screen.ScreenKey
import com.companion.lol.domain.usecase.DeleteFavorites
import com.companion.lol.domain.usecase.ObserveChampion
import com.companion.lol.domain.usecase.ObserveSettings
import com.companion.lol.domain.usecase.RefreshChampion
import com.companion.lol.domain.usecase.UpdateSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ChampionViewModel
@Inject
constructor(
  private val backStack: BackStack<ScreenKey>,
  private val refreshChampions: RefreshChampion,
  private val updateSettings: UpdateSettings,
  private val deleteFavorites: DeleteFavorites,
  private val observeChampions: ObserveChampion,
  observeSession: ObserveSettings,
) : ViewModel() {
  private val refreshState = MutableStateFlow(ChampionRefreshState())

  val state: StateFlow<ChampionState> =
    combine(
        flow = observeChampions(),
        flow2 = observeSession(),
        flow3 = refreshState,
        transform = { champion, settings, refreshState ->
          ChampionState(
            // few champions, otherwise flowOn down the line
            // or sort in db query
            champions = champion.sortBy(settings.championSortOrder),
            gridSize = settings.championGridSize,
            sortOrder = settings.championSortOrder,
            refreshState = refreshState,
          )
        },
      )
      .stateIn(viewModelScope, SharingStarted.Eagerly, ChampionState())

  init {
    viewModelScope.launch {
      refreshState.filter { it.refreshing }.map { it.userTriggered }.collectLatest(::refresh)
    }
  }

  fun onRefresh() {
    refreshState.value =
      ChampionRefreshState(refreshing = true, userTriggered = true, hasError = false)
  }

  fun onRetry() = onRefresh()

  private suspend fun refresh(userTriggered: Boolean) {
    val success = refreshChampions(userTriggered)

    refreshState.value =
      ChampionRefreshState(refreshing = false, userTriggered = userTriggered, hasError = !success)
  }

  fun changeGridSize() {
    viewModelScope.launch { updateSettings(gridSize = state.value.gridSize.next()) }
  }

  fun onSortMenuItemClicked() {
    viewModelScope.launch { updateSettings(sortOrder = state.value.sortOrder.toggle()) }
  }

  fun onFavoritesClearClicked() {
    viewModelScope.launch { deleteFavorites() }
  }

  fun onCardClick(championId: ChampionId) {
    backStack.goTo(ChampionDetailsKey(championId))
  }
}
