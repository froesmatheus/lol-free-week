package com.matheusfroes.lolfreeweek.jobs

import androidx.work.Worker
import com.matheusfroes.lolfreeweek.CustomApplication
import com.matheusfroes.lolfreeweek.extra.NotificationSender
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
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

        return Result.SUCCESS
    }
}