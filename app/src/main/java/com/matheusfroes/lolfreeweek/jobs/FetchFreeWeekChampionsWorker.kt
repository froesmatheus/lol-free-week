package com.matheusfroes.lolfreeweek.jobs

import androidx.work.Worker
import com.matheusfroes.lolfreeweek.CustomApplication
import com.matheusfroes.lolfreeweek.NotificationSender
import javax.inject.Inject

class FetchFreeWeekChampionsWorker : Worker() {


    @Inject
    lateinit var notificationSender: NotificationSender

    override fun doWork(): Result {
        (applicationContext as CustomApplication).injector.inject(this)

        notificationSender.notifyUser()
        return Result.SUCCESS
    }

}