package com.companion.lol.ui.settings

import com.companion.lol.core.io.SyncWorkerDispatcher
import kotlin.time.Duration

class SyncWorkerDispatcherFake : SyncWorkerDispatcher {
  var scheduleCount = 0
  var cancelCount = 0

  override fun schedulePeriodicSync(repeatInterval: Duration, startAfterInterval: Boolean) {
    scheduleCount++
  }

  override fun cancelPeriodicSync() {
    cancelCount++
  }
}
