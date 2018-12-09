package com.matheusfroes.lolfreeweek.jobs

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.Worker
import com.matheusfroes.lolfreeweek.CustomApplication
import com.matheusfroes.lolfreeweek.extra.NotificationSender
import com.matheusfroes.lolfreeweek.extra.createFreeWeekWorkRequest
import timber.log.Timber
import javax.inject.Inject

class FetchFreeWeekChampionsWorker : Worker() {


    @Inject
    lateinit var notificationSender: NotificationSender

    override fun doWork(): Result {
        Timber.d("FetchFreeWeekChampionsWorker doWork()")
        (applicationContext as CustomApplication).injector.inject(this)

        notificationSender.fetchFreeWeekChampions()

        val workRequest = createFreeWeekWorkRequest()

        WorkManager.getInstance()
                .beginUniqueWork("FETCH_FW", ExistingWorkPolicy.REPLACE, workRequest)
                .enqueue()

        return Result.SUCCESS
    }
}