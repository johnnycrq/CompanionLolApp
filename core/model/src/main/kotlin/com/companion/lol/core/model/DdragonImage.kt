package com.companion.lol.core.model

import androidx.compose.runtime.Immutable

@Immutable
interface DdragonImage {
  val imageUrl: String

  @Immutable data class Square(override val imageUrl: String) : DdragonImage

  @Immutable data class Skin(val skinName: String, override val imageUrl: String) : DdragonImage
}
