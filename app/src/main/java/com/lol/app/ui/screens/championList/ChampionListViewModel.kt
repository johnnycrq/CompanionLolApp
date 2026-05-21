package com.lol.app.ui.screens.championList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.data.usecase.ChampionUseCase
import com.companion.lol.data.usecase.RefreshChampionsUseCase
import com.companion.lol.data.usecase.SettingsUseCase
import com.companion.lol.storage.impl.model.other.SortOrder
import com.lol.app.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ChampionListViewModel
@Inject
constructor(
  private val settingsUseCase: SettingsUseCase,
  private val championUseCase: ChampionUseCase,
  private val refreshChampionsUseCase: RefreshChampionsUseCase,
) : ViewModel() {

  private val isRefreshing = MutableStateFlow(false)

  val state: StateFlow<ChampionListState> =
    combine(
        flow = championUseCase.observeAllWithFavorites(),
        flow2 = settingsUseCase.observe(),
        flow3 = isRefreshing,
        transform = { champion, settings, isRefreshing ->
          ChampionListState(
            champions = champion.sortBy(settings.championRotationSortOrder),
            gridSize = settings.championRotationGridSize,
            sortOrder = settings.championRotationSortOrder,
            isRefreshing = isRefreshing,
          )
        },
      )
      .stateIn(viewModelScope, SharingStarted.Eagerly, ChampionListState())

  init {
    viewModelScope.launch { refresh(false) }
  }

  fun onRefresh() {
    viewModelScope.launch { refresh(true) }
  }

  private suspend fun refresh(force: Boolean) {
    if (force || !refreshChampionsUseCase.hasData()) {
      try {
        isRefreshing.value = true
        refreshChampionsUseCase.refresh()
        // simulated delay to check animation
        delay(5000)
      } finally {
        isRefreshing.value = false
      }
    }
  }

  fun changeGridSize() {
    viewModelScope.launch {
      settingsUseCase.updateChampionGridSize(
        when (val value = state.value.gridSize) {
          5 -> 3
          else -> value + 1
        }
      )
    }
  }

  fun onSortMenuItemClicked() {
    viewModelScope.launch {
      settingsUseCase.updateChampionSortOrder(state.value.sortOrder.toggle())
    }
  }

  fun onFavoritesClearClicked() {
    viewModelScope.launch { championUseCase.clearFavorites() }
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
