package com.companion.lol.ui.settings

import app.cash.turbine.test
import com.companion.lol.domain.model.UserSession
import com.companion.lol.domain.usecase.DeleteSessionFake
import com.companion.lol.domain.usecase.ObserveSessionFake
import com.companion.lol.domain.usecase.UpdateSessionFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

  class ViewModelContainer(
    val observeSession: ObserveSessionFake = ObserveSessionFake(),
    val updateSession: UpdateSessionFake = UpdateSessionFake(),
    val deleteSession: DeleteSessionFake = DeleteSessionFake(),
    val syncWorkerDispatcher: SyncWorkerDispatcherFake = SyncWorkerDispatcherFake(),
  ) {
    val viewModel =
      SettingsViewModel(
        observeSession = observeSession,
        updateSession = updateSession,
        deleteSession = deleteSession,
        syncWorkerDispatcher = syncWorkerDispatcher,
      )
  }

  private fun viewModelContainer() = ViewModelContainer()

  @Before
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `initial state is null`() = runTest {
    viewModelContainer().viewModel.state.test {
      assertEquals(null, awaitItem())
    }
  }

  @Test
  fun `state updates when session is observed`() = runTest {
    val container = viewModelContainer()

    container.viewModel.state.test {
      val session = UserSession.Authenticated("test@example.com", true)

      container.observeSession.emit(session)
      advanceUntilIdle()

      val item = checkNotNull(expectMostRecentItem())
      assertEquals(session.emailAddress, item.emailAddress)
      assertEquals(session.autoSync, item.autoSync)
    }
  }

  @Test
  fun `onAutoSyncChanged updates session and schedules sync when enabled`() = runTest {
    val container = viewModelContainer()

    container.viewModel.state.test {
      skipItems(1)
      container.viewModel.onAutoSyncChanged(true)
      advanceUntilIdle()

      assertEquals(1, container.updateSession.callCount)
      assertEquals(true, container.updateSession.lastAutoSync)
      assertEquals(1, container.syncWorkerDispatcher.scheduleCount)
      assertEquals(0, container.syncWorkerDispatcher.cancelCount)
    }
  }

  @Test
  fun `onAutoSyncChanged updates session and cancels sync when disabled`() = runTest {
    val container = viewModelContainer()

    container.viewModel.state.test {
      skipItems(1)

      container.viewModel.onAutoSyncChanged(false)
      advanceUntilIdle()

      assertEquals(1, container.updateSession.callCount)
      assertEquals(false, container.updateSession.lastAutoSync)
      assertEquals(0, container.syncWorkerDispatcher.scheduleCount)
      assertEquals(1, container.syncWorkerDispatcher.cancelCount)
    }
  }
}
