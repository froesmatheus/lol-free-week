package com.matheusfroes.lolfreeweek.jobs

import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.matheusfroes.lolfreeweek.NotificationSender
import com.matheusfroes.lolfreeweek.getDateDiff
import com.matheusfroes.lolfreeweek.nextDayOfWeek
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by matheusfroes on 12/02/2017.
 */
class CreateFirstFetchJob : Job() {
    val notificationSender by lazy {
        NotificationSender(context)
    }

    companion object {
        val TAG = "create_first_fetch_job"

        fun scheduleFirstWeeklyJob() {
            val today = nextDayOfWeek(Calendar.TUESDAY)
            today.set(Calendar.HOUR_OF_DAY, 0)
            today.set(Calendar.MINUTE, 0)
            today.set(Calendar.SECOND, 0)
            today.set(Calendar.MILLISECOND, 0)
            val nextTuesday = Calendar.getInstance()
            nextTuesday.set(Calendar.HOUR_OF_DAY, 0)
            nextTuesday.set(Calendar.MINUTE, 0)
            nextTuesday.set(Calendar.SECOND, 0)
            nextTuesday.set(Calendar.MILLISECOND, 0)

            val differenceInMillis = getDateDiff(nextTuesday.time, today.time, TimeUnit.MILLISECONDS)

            val jobId = JobRequest.Builder(TAG)
                    .setExact(differenceInMillis)
                    .setPersisted(true)
                    .build()
                    .schedule()

            Log.d("LOL-FREE-WEEK", "Job was scheduled")
        }
    }

    override fun onRunJob(params: Params?): Result {
        val jobId = JobRequest.Builder(FetchFreeWeekChampionsJob.TAG)
                .setPeriodic(TimeUnit.DAYS.toMillis(7), TimeUnit.MINUTES.toMillis(5))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build()
                .schedule()

        Log.d("LOL-FREE-WEEK", "Job was triggered")
        notificationSender.notifyUser()

        return Result.SUCCESS
    }
}