package com.lol.app.ui.screens.championList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.app.BuildConfig
import com.companion.lol.data.mapper.model
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.data.usecase.RefreshChampionsUseCase
import com.companion.lol.storage.impl.model.other.SortOrder
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SettingsStore
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import com.companion.lol.util.listMap
import com.lol.app.ui.screens.RefreshState
import com.lol.app.util.awaitAtLeast
import com.lol.app.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val showCaseArtificialAnimationDelay = if (BuildConfig.DEBUG) 2.seconds else Duration.ZERO

@HiltViewModel
class ChampionListViewModel
@Inject
constructor(
  private val championStore: ChampionStore,
  private val settingsStore: SettingsStore,
  private val favoritesStore: ChampionFavoritesStore,
  private val refreshChampionsUseCase: RefreshChampionsUseCase,
) : ViewModel() {
  private val refreshState = MutableStateFlow(RefreshState())

  val state: StateFlow<ChampionListState> =
    combine(
        flow = championStore.observeAllWithFavorites().listMap(ChampionWithFavoritesView::model),
        flow2 = settingsStore.observe(),
        flow3 = refreshState,
        transform = { champion, settings, refreshState ->
          ChampionListState(
            champions = champion.sortBy(settings.championRotationSortOrder),
            gridSize = settings.championRotationGridSize,
            sortOrder = settings.championRotationSortOrder,
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
    refreshState.value = RefreshState(refreshing = true, userTriggered = true, hasError = false)
  }

  fun onRetry() = onRefresh()

  private suspend fun refresh(userTriggered: Boolean) {
    if (userTriggered || !championStore.hasData()) {
      val success =
        awaitAtLeast(showCaseArtificialAnimationDelay) { refreshChampionsUseCase.refresh() }

      refreshState.value =
        RefreshState(refreshing = false, userTriggered = userTriggered, hasError = !success)
    } else {
      delay(showCaseArtificialAnimationDelay)
      refreshState.value = RefreshState(refreshing = false, userTriggered = false, hasError = false)
    }
  }

  fun changeGridSize() {
    viewModelScope.launch {
      settingsStore.insert(
        championRotationGridSize =
          when (state.value.gridSize) {
            3 -> 4
            4 -> 5
            else -> 3
          }
      )
    }
  }

  fun onSortMenuItemClicked() {
    viewModelScope.launch {
      settingsStore.insert(championRotationSortOrder = state.value.sortOrder.toggle())
    }
  }

  fun onFavoritesClearClicked() {
    viewModelScope.launch { favoritesStore.clearAll() }
  }

  private fun List<ChampionModel>.sortBy(order: SortOrder): List<ChampionModel> {
    return when (order) {
      SortOrder.FAVORITES ->
        this.sortedWith(
          compareBy(
            {
              when (it.isFavorite) {
                true -> 1
                else -> 2
              }
            },
            { it.name },
          )
        )

      SortOrder.ASC -> this.sortedBy { it.name }
    }
  }
}
