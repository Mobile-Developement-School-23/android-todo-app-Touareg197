package ru.lobanov.todoapp.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


object WorkManagerScheduler {

    private const val DELAY = 480L
    private const val REPEAT_INTERVAL = 24L
    private const val WORK_TAG = "myWorkManager"
    private const val UNIQUE_WORK_NAME = "refreshWork"

    fun refreshPeriodicWork(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val refreshWork = PeriodicWorkRequest
            .Builder(MyWorker::class.java, REPEAT_INTERVAL, TimeUnit.HOURS)
            .setInitialDelay(DELAY, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE, refreshWork
        )
    }
}