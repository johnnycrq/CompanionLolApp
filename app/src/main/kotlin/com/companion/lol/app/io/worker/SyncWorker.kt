package com.companion.lol.app.io.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.companion.lol.core.io.SyncWorkerDispatcher
import com.companion.lol.domain.usecase.RefreshChampion
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@HiltWorker
class SyncWorker
@AssistedInject
constructor(
  @Assisted context: Context,
  @Assisted workerParams: WorkerParameters,
  private val refreshChampions: Lazy<RefreshChampion>,
) : CoroutineWorker(context, workerParams) {
  companion object : SyncWorkerDispatcher {
    private const val PERIODIC_WORK_NAME = "periodic_sync"

    override fun schedulePeriodicSync(
      context: Context,
      repeatInterval: Duration,
      startAfterInterval: Boolean,
    ) {
      val workManager = WorkManager.getInstance(context)
      val duration = repeatInterval.toJavaDuration()
      val periodicSync =
        PeriodicWorkRequestBuilder<SyncWorker>(duration)
          .setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
          )
          .apply {
            if (startAfterInterval) {
              setInitialDelay(duration)
            }
          }
          .build()

      workManager.enqueueUniquePeriodicWork(
        PERIODIC_WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        periodicSync,
      )
    }

    override fun cancelPeriodicSync(context: Context) {
      WorkManager.getInstance(context).cancelUniqueWork(PERIODIC_WORK_NAME)
    }
  }

  override suspend fun doWork(): Result {
    return refreshChampions.get().invoke(forceRefresh = true).let {
      if (it) Result.success() else Result.retry()
    }
  }
}
