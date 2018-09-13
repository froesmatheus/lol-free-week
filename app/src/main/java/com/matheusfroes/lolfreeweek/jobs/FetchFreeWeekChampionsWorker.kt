package com.matheusfroes.lolfreeweek.jobs

import androidx.work.*
import com.matheusfroes.lolfreeweek.CustomApplication
import com.matheusfroes.lolfreeweek.extra.NotificationSender
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FetchFreeWeekChampionsWorker : Worker() {


    @Inject
    lateinit var notificationSender: NotificationSender

    override fun doWork(): Result {
        Timber.d("FetchFreeWeekChampionsWorker doWork()")
        (applicationContext as CustomApplication).injector.inject(this)

        launch {
            notificationSender.fetchFreeWeekChampions()
        }


        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val workRequest = OneTimeWorkRequestBuilder<FetchFreeWeekChampionsWorker>()
                .setInitialDelay(30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance()
                .beginUniqueWork("FETCH_FREE_WEEK", ExistingWorkPolicy.KEEP, workRequest)
                .enqueue()
        return Result.SUCCESS
    }
}