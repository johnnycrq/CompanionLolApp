package com.companion.lol.ui.login

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

  private val viewModel = { updateSession: UpdateSessionFake ->
    LoginViewModel(updateSession = updateSession, savedStateHandle = SavedStateHandle())
  }

  @Before
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `initial state is empty and invalid`() = runTest {
    val updateSession = UpdateSessionFake()
    val viewModel = viewModel(updateSession)

    assertEquals("", viewModel.state.value.email)
    assertFalse(viewModel.state.value.isEmailValid)
  }

  @Test
  fun `onEmailChanged updates email state`() = runTest {
    val updateSession = UpdateSessionFake()
    val viewModel = viewModel(updateSession)

    val email = "test@example.com"
    viewModel.onEmailChanged(email)

    assertEquals(email, viewModel.state.value.email)
    assertTrue(viewModel.state.value.isEmailValid)
  }

  @Test
  fun `onLoginClicked does not update session if email is invalid`() = runTest {
    val updateSession = UpdateSessionFake()
    val viewModel = viewModel(updateSession)

    viewModel.onEmailChanged("invalid-email")
    viewModel.onLoginClicked()
    advanceUntilIdle()

    assertEquals(0, updateSession.callCount)
  }

  @Test
  fun `onLoginClicked updates session if email is valid`() = runTest {
    val updateSession = UpdateSessionFake()
    val viewModel = viewModel(updateSession)

    val email = "test@example.com"
    viewModel.onEmailChanged(email)
    viewModel.onLoginClicked()
    advanceUntilIdle()

    assertEquals(1, updateSession.callCount)
    assertEquals(email, updateSession.lastEmailAddress)
    assertFalse(updateSession.lastAutoSync ?: true)
  }
}
