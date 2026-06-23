package com.companion.lol.app.io.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.companion.lol.domain.usecase.RefreshChampion
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker
@AssistedInject
constructor(
  @Assisted context: Context,
  @Assisted workerParams: WorkerParameters,
  private val refreshChampions: Lazy<RefreshChampion>,
) : CoroutineWorker(context, workerParams) {
  companion object {
    const val PERIODIC_WORK_NAME = "periodic_sync"
  }

  override suspend fun doWork(): Result {
    return refreshChampions.get().invoke(forceRefresh = true).let {
      if (it) Result.success() else Result.retry()
    }
  }
}
