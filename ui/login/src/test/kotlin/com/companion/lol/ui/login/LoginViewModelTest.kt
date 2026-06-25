package com.companion.lol.ui.login

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
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

  private class ViewModelContainer(
    val updateSession: UpdateSessionFake = UpdateSessionFake(),
    val savedStateHandle: SavedStateHandle = SavedStateHandle(),
  ) {

    val viewModel =
      LoginViewModel(updateSession = updateSession, savedStateHandle = savedStateHandle)
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
    val container = ViewModelContainer()
    container.viewModel.state.test {
      val item = awaitItem()

      assertEquals(LoginState(), item)
      assertFalse(item.isEmailValid)
    }
  }

  @Test
  fun `onEmailChanged updates email state`() = runTest {
    val container = ViewModelContainer()
    container.viewModel.state.test {
      val email = "test@example.com"
      container.viewModel.onEmailChanged(email)
      val item = expectMostRecentItem()

      assertEquals(email, item.email)
      assertTrue(item.isEmailValid)
    }
  }

  @Test
  fun `onLoginClicked does not update session if email is invalid`() = runTest {
    val container = ViewModelContainer()

    container.viewModel.state.test {
      val email = "test@example"
      container.viewModel.onEmailChanged(email)
      container.viewModel.onLoginClicked()
      advanceUntilIdle()

      val item = expectMostRecentItem()

      assertEquals(email, item.email)
      assertFalse(item.isEmailValid)
      assertEquals(0, container.updateSession.callCount)
    }
  }

  @Test
  fun `onLoginClicked updates session if email is valid`() = runTest {
    val container = ViewModelContainer()

    container.viewModel.state.test {
      val email = "test@example.com"
      container.viewModel.onEmailChanged(email)
      container.viewModel.onLoginClicked()
      advanceUntilIdle()

      val item = expectMostRecentItem()

      assertEquals(email, item.email)
      assertTrue(item.isEmailValid)

      assertEquals(1, container.updateSession.callCount)
    }
  }
}
