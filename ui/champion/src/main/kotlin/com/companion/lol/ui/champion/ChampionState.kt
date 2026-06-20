package com.companion.lol.ui.champion

import androidx.compose.runtime.Stable
import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.SortOrder
import com.companion.lol.core.ui.state.ComposeState
import com.companion.lol.domain.model.Champion

@Stable
data class ChampionState(
  val champions: List<Champion> = emptyList(),
  val gridSize: GridSize = GridSize.MEDIUM,
  val sortOrder: SortOrder = SortOrder.ASC,
  private val refreshState: ChampionRefreshState = ChampionRefreshState(),
) : ComposeState {
  val isRefreshing: Boolean = refreshState.refreshing

  val showError: Boolean = refreshState.hasError && champions.isEmpty()
}
