package com.matheusfroes.lolfreeweek.jobs

import android.util.Log
import com.evernote.android.job.Job
import com.matheusfroes.lolfreeweek.NotificationSender

/**
 * Created by matheusfroes on 12/02/2017.
 */
class FetchFreeWeekChampionsJob : Job() {
    val notificationSender by lazy {
        NotificationSender(context)
    }


    companion object {
        val TAG = "fetch_free_week_champions_job"
    }

    override fun onRunJob(params: Params?): Result {
        Log.d("LOL-FREE-WEEK", "Fetch Free Week Job just triggered")

        notificationSender.notifyUser()


        return Result.SUCCESS
    }
}
