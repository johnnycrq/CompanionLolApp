@file:Suppress("ANNOTATION_WILL_BE_APPLIED_ALSO_TO_PROPERTY_OR_FIELD")

package com.companion.lol.core.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import java.util.UUID

@Immutable
sealed class UiError {
  abstract val id: Long

  @Composable abstract fun resolve(): String

  data class Text(val message: String) : UiError() {
    override val id: Long = UUID.randomUUID().mostSignificantBits

    @Composable override fun resolve(): String = message
  }

  data class Resource(@StringRes val resourceId: Int) : UiError() {
    override val id: Long = UUID.randomUUID().mostSignificantBits

    @Composable override fun resolve(): String = stringResource(id = resourceId)
  }
}
