package com.companion.lol.core.io

import kotlin.time.Duration

interface SyncWorkerDispatcher {
  fun schedulePeriodicSync(
    repeatInterval: Duration = AppConst.syncRepeatDuration,
    startAfterInterval: Boolean = true,
  )

  fun cancelPeriodicSync()
}
