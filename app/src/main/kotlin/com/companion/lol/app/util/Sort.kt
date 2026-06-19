package com.companion.lol.app.util

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
