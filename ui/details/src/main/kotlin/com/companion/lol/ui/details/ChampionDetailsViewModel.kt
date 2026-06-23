package com.companion.lol.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.core.model.ChampionId
import com.companion.lol.core.ui.MessagePoster
import com.companion.lol.core.ui.UiError
import com.companion.lol.core.ui.navigation.BackStack
import com.companion.lol.domain.usecase.RefreshChampionDetails
import com.companion.lol.domain.usecase.UpdateFavorites
import com.companion.lol.domain.usecase.impl.ObserveChampionDetails
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.time.Duration.Companion.seconds
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
  private val messagePoster: MessagePoster,
  private val backStack: BackStack,
  private val refreshChampionDetails: RefreshChampionDetails,
  private val observeChampionDetails: ObserveChampionDetails,
  private val updateFavorites: UpdateFavorites,
) : ViewModel() {
  @AssistedFactory
  interface Factory {
    fun create(championId: ChampionId): ChampionDetailsViewModel
  }

  val state: StateFlow<ChampionDetailsState> =
    observeChampionDetails(championId = championId)
      .map { ChampionDetailsState(championId = championId, championWithDetails = it) }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChampionDetailsState(championId = championId),
      )

  init {
    viewModelScope.launch {
      // refresh details
      val success =
        refreshChampionDetails(retry = 2, retryDelay = 2.seconds, championId = championId)

      if (success) return@launch

      if (!state.value.hasData) {
        Log.i("uiError", "uiError: error")
        messagePoster.postMessage(
          UiError.Resource(resourceId = R.string.champion_details_error_load)
        )
        backStack.goBack()
      }
    }
  }

  fun onFavoritesClicked() {
    val champion = state.value.champion ?: return

    viewModelScope.launch {
      updateFavorites(championId = champion.id, isFavorite = champion.isFavorite.not())
    }
  }
}
