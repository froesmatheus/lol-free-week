package com.matheusfroes.lolfreeweek.jobs

import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.matheusfroes.lolfreeweek.extra.NotificationSender
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by matheusfroes on 12/02/2017.
 */
class FetchFreeWeekChampionsJob @Inject constructor(
        private val notificationSender: NotificationSender
) : Job() {

    companion object {
        const val TAG = "fetch_free_week_champions_job"

        fun scheduleFreeWeekJob() {
            val jobId = JobRequest.Builder(TAG)
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                    .setUpdateCurrent(true)
                    .build()
                    .schedule()

            Log.d("LOL-FREE-WEEK", "Job was scheduled ID = $jobId")
        }
    }

    override fun onRunJob(params: Params): Result {
        Log.d("LOL-FREE-WEEK", "Fetch Free Week Job just triggered")

        notificationSender.notifyUser()


        return Result.SUCCESS
    }
}
