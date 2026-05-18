package com.lol.app.ui.screens.championList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.GetChampionUseCase
import com.companion.lol.data.usecase.SettingsUseCase
import com.companion.lol.storage.impl.model.ids.ChampionId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ChampionListViewModel
@Inject
constructor(
    private val getChampion: GetChampionUseCase,
    private val settings: SettingsUseCase,
) : ViewModel() {

  val state: StateFlow<ChampionListState> =
    combine(
        flow = getChampion.observeAllWithFavorites(),
        flow2 = settings.observe(),
        transform = { champion, settings ->
          ChampionListState(
            champions = champion,
            gridSize = settings.championRotationGridSize,
            sortOrder = settings.championRotationSortOrder,
          )
        },
      )
      .stateIn(viewModelScope, SharingStarted.Eagerly, ChampionListState())

    fun onCardClick(championId: ChampionId){}

    fun changeGridSize() {
        viewModelScope.launch {
            settings.updateChampionRotationGridSize(
                when (val value = state.value.gridSize) {
                    5 -> 3
                    else -> value + 1
                }
            )
        }
    }
}
