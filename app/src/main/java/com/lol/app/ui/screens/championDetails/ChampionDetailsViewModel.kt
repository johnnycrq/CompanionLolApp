package com.lol.app.ui.screens.championDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.ChampionUseCase
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.util.dbDispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ChampionDetailsViewModel.Factory::class)
class ChampionDetailsViewModel
@AssistedInject
constructor(@Assisted championId: ChampionId, private val championUseCase: ChampionUseCase) :
  ViewModel() {
  @AssistedFactory
  interface Factory {
    fun create(championId: ChampionId): ChampionDetailsViewModel
  }

  val state: StateFlow<ChampionDetailsState> =
    championUseCase
      .observeChampionWithDetails(championId)
      .flowOn(dbDispatcher)
      .map {
        ChampionDetailsState(championId = championId, champion = it.champion, details = it.details)
      }
      .stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        ChampionDetailsState(championId = championId),
      )

  fun onFavoritesClicked() {
    val champion = state.value.champion ?: return

    viewModelScope.launch {
      championUseCase.markFavourite(
        championId = champion.id,
        isFavourite = champion.isFavorite.not(),
      )
    }
  }
}
