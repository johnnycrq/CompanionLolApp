package com.companion.lol.app.io.worker

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.companion.lol.core.io.SyncWorkerDispatcher
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Singleton
class SyncWorkerDispatcherImpl @Inject constructor(private val context: Application) :
  SyncWorkerDispatcher {
  override fun schedulePeriodicSync(repeatInterval: Duration, startAfterInterval: Boolean) {
    val workManager = WorkManager.getInstance(context)
    val duration = repeatInterval.toJavaDuration()
    val periodicSync =
      PeriodicWorkRequestBuilder<SyncWorker>(duration)
        .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
        .apply {
          if (startAfterInterval) {
            setInitialDelay(duration)
          }
        }
        .build()

    workManager.enqueueUniquePeriodicWork(
      SyncWorker.PERIODIC_WORK_NAME,
      ExistingPeriodicWorkPolicy.KEEP,
      periodicSync,
    )
  }

  override fun cancelPeriodicSync() {
    WorkManager.getInstance(context).cancelUniqueWork(SyncWorker.PERIODIC_WORK_NAME)
  }
}
