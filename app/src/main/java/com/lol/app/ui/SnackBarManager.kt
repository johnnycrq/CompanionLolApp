package com.lol.app.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Stable
interface SnackBarManager {
  @Stable val snackBarHostState: SnackbarHostState

  suspend fun addError(error: String)

  suspend fun startShowPendingErrors()
}

private class Impl(override val snackBarHostState: SnackbarHostState) : SnackBarManager {
  // We want a maximum of 3 errors queued
  private val pendingErrors = Channel<String>(3, BufferOverflow.DROP_OLDEST)

  override suspend fun startShowPendingErrors() {
    pendingErrors.receiveAsFlow().collect { error ->
      snackBarHostState.showSnackbar(error, duration = SnackbarDuration.Short)
    }
  }

  /** Add [error] to the queue of errors to display. */
  override suspend fun addError(error: String) {
    pendingErrors.send(error)
  }
}

@Composable
fun rememberSnackBarManager(): SnackBarManager {
  return remember { Impl(SnackbarHostState()) }
}
