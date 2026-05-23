package com.lol.app.ui.screens.championDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.ChampionWithDetailsUseCase
import com.companion.lol.data.usecase.RefreshChampionDetailsUseCase
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import com.companion.lol.util.withRetry
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ChampionDetailsViewModel.Factory::class)
class ChampionDetailsViewModel
@AssistedInject
constructor(
  @Assisted private val championId: ChampionId,
  detailsUseCase: ChampionWithDetailsUseCase,
  private val refreshUseCase: RefreshChampionDetailsUseCase,
  private val favoritesStore: ChampionFavoritesStore,
) : ViewModel() {
  @AssistedFactory
  interface Factory {
    fun create(championId: ChampionId): ChampionDetailsViewModel
  }

  val errorOnFetch: ReceiveChannel<Unit>
    field = Channel(capacity = 1)

  val state: StateFlow<ChampionDetailsState> =
    detailsUseCase
      .observeChampionWithDetails(championId = championId)
      .map {
        ChampionDetailsState(championId = championId, champion = it.champion, details = it.details)
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChampionDetailsState(championId = championId),
      )

  init {
    viewModelScope.launch {
      // refresh details
      withRetry(times = 3, delayDuration = 5.seconds) { refreshUseCase.refresh(championId) }
        .onFailure { errorOnFetch.send(Unit) }
    }
  }

  fun onFavoritesClicked() {
    val champion = state.value.champion ?: return

    viewModelScope.launch {
      favoritesStore.markFavorite(championId = champion.id, isFavorite = champion.isFavorite.not())
    }
  }
}
