package com.lol.app

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.companion.lol.data.usecase.RefreshChampionsUseCase
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshChampions: Lazy<RefreshChampionsUseCase>
): CoroutineWorker(context, workerParams) {
    companion object{

        private const val PERIODIC_WORK_NAME = "periodic_sync"
        fun initialSync(
            context: Context
        ){
            val workManager = WorkManager.getInstance(context)

            // we use the existence of the periodic sync work to
            // check if we need the initial sync
            val workInfos = workManager.getWorkInfosForUniqueWork(PERIODIC_WORK_NAME)
                .get()

            val hasExistingWork = workInfos.any { info ->
                info.state == WorkInfo.State.ENQUEUED ||
                        info.state == WorkInfo.State.RUNNING ||
                        info.state == WorkInfo.State.BLOCKED
            }

            if(!hasExistingWork){
                // 1) Immediate sync (runs ASAP once)
                workManager.enqueue(
                    OneTimeWorkRequestBuilder<SyncWorker>()
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setConstraints(
                            Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                        )
                        .build()
                )
            }

            // 2) Repeating sync every 2 days
            val periodicSync = PeriodicWorkRequestBuilder<SyncWorker>(
                2, TimeUnit.DAYS
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setInitialDelay(2, TimeUnit.DAYS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicSync
            )
        }
    }
    override suspend fun doWork(): Result {
        return try {
            refreshChampions.get().refresh()
            Result.success()
        }catch (e: Exception){
            Timber.e(e)
            Result.retry()
        }
    }
}
