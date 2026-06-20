package com.companion.lol.ui.champion

import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.SortOrder
import com.companion.lol.domain.model.Champion

fun List<Champion>.sortBy(order: SortOrder): List<Champion> {
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

fun GridSize.next(): GridSize = GridSize.entries.run { get((ordinal + 1) % size) }

fun SortOrder.toggle() =
  when (this) {
    SortOrder.FAVORITES -> SortOrder.ASC
    SortOrder.ASC -> SortOrder.FAVORITES
  }

val GridSize.count: Int
  get() =
    when (this) {
      GridSize.SMALL -> 5
      GridSize.MEDIUM -> 4
      GridSize.LARGE -> 3
    }
