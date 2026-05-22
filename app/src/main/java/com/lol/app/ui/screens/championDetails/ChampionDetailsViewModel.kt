package com.lol.app.ui.screens.championDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.ChampionUseCase
import com.companion.lol.storage.impl.model.ids.ChampionId
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
      .observeChampionWithDetails(championId = championId, refetch = true)
      .map {
        ChampionDetailsState(championId = championId, champion = it.champion, details = it.details)
      }
      .stateIn(
        scope = viewModelScope,
        // This screen of this state may need network fetching. We use WhileSubscribed
        // in combination with collectAsStateWithLifecycle so it retriggers the fetch
        // from background-foreground
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
        initialValue = ChampionDetailsState(championId = championId),
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
