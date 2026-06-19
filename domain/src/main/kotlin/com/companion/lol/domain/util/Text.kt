package com.companion.lol.domain.util

fun String.capitalizeWords(): String =
  split(" ").joinToString(" ") {
    it.lowercase().replaceFirstChar { char ->
      if (char.isLowerCase()) char.titlecase() else char.toString()
    }
  }
