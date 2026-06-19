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
import com.companion.lol.data.mapper.toModel
import com.companion.lol.data.usecase.RefreshChampionsUseCase
import com.companion.lol.data.util.listMap
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SettingsStore
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
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
  private val championStore: ChampionStore,
  private val favoritesStore: ChampionFavoritesStore,
  private val settingsStore: SettingsStore,
  private val refreshChampionsUseCase: RefreshChampionsUseCase,
) : ViewModel() {
  private val refreshState = MutableStateFlow(DataRefreshState())

  val state: StateFlow<ChampionListState> =
    combine(
        flow = championStore.observeAllWithFavorites().listMap(ChampionWithFavoritesView::toModel),
        flow2 = settingsStore.observeOrDefault(),
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
    if (userTriggered || !championStore.hasData()) {
      val success = refreshChampionsUseCase.refresh().isSuccess

      refreshState.value =
        DataRefreshState(refreshing = false, userTriggered = userTriggered, hasError = !success)
    } else {
      refreshState.value =
        DataRefreshState(refreshing = false, userTriggered = false, hasError = false)
    }
  }

  fun changeGridSize() {
    viewModelScope.launch { settingsStore.insert(championGridSize = state.value.gridSize.next()) }
  }

  fun onSortMenuItemClicked() {
    viewModelScope.launch {
      settingsStore.insert(championSortOrder = state.value.sortOrder.toggle())
    }
  }

  fun onFavoritesClearClicked() {
    viewModelScope.launch { favoritesStore.clearAll() }
  }

  fun onCardClick(championId: ChampionId) {
    backStack.goTo(ChampionDetailsKey(championId))
  }
}
