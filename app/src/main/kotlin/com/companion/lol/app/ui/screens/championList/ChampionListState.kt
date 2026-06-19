package com.companion.lol.app.ui.screens.championList

import androidx.compose.runtime.Stable
import com.companion.lol.app.base.ComposeState
import com.companion.lol.app.ui.screens.DataRefreshState
import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.SortOrder
import com.companion.lol.domain.model.Champion

@Stable
data class ChampionListState(
  val champions: List<Champion> = emptyList(),
  val gridSize: GridSize = GridSize.MEDIUM,
  val sortOrder: SortOrder = SortOrder.ASC,
  private val refreshState: DataRefreshState = DataRefreshState(),
) : ComposeState {
  val isRefreshing: Boolean = refreshState.refreshing

  val showError: Boolean = refreshState.hasError && champions.isEmpty()
}
