@file:Suppress("CanBeParameter")

package com.companion.lol.app.ui.screens.championList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.keys.ChampionDetailsKey
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.ui.screens.DataRefreshState
import com.companion.lol.app.util.next
import com.companion.lol.app.util.sortBy
import com.companion.lol.app.util.toggle
import com.companion.lol.core.model.ChampionId
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
class ChampionListViewModel
@Inject
constructor(
  private val backStack: BackStack<ScreenKey>,
  private val refreshChampions: RefreshChampion,
  private val updateSettings: UpdateSettings,
  private val deleteFavorites: DeleteFavorites,
  private val observeChampions: ObserveChampion,
  observeSession: ObserveSettings,
) : ViewModel() {
  private val refreshState = MutableStateFlow(DataRefreshState())

  val state: StateFlow<ChampionListState> =
    combine(
        flow = observeChampions(),
        flow2 = observeSession(),
        flow3 = refreshState,
        transform = { champion, settings, refreshState ->
          ChampionListState(
            // few champions, otherwise flowOn down the line
            // or sort in db query
            champions = champion.sortBy(settings.championSortOrder),
            gridSize = settings.championGridSize,
            sortOrder = settings.championSortOrder,
            refreshState = refreshState,
          )
        },
      )
      .stateIn(viewModelScope, SharingStarted.Eagerly, ChampionListState())

  init {
    viewModelScope.launch {
      refreshState.filter { it.refreshing }.map { it.userTriggered }.collectLatest(::refresh)
    }
  }

  fun onRefresh() {
    refreshState.value = DataRefreshState(refreshing = true, userTriggered = true, hasError = false)
  }

  fun onRetry() = onRefresh()

  private suspend fun refresh(userTriggered: Boolean) {
    val success = refreshChampions(userTriggered)

    refreshState.value =
      DataRefreshState(refreshing = false, userTriggered = userTriggered, hasError = !success)
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
