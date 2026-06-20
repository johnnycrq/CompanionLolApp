package com.companion.lol.core.io

import android.content.Context
import kotlin.time.Duration

interface SyncWorkerDispatcher {
  fun schedulePeriodicSync(
    context: Context,
    repeatInterval: Duration = AppConst.syncRepeatDuration,
    startAfterInterval: Boolean = true,
  )

  fun cancelPeriodicSync(context: Context)
}
