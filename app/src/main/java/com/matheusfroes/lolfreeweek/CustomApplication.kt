package com.matheusfroes.lolfreeweek

import android.app.Application
import com.evernote.android.job.JobManager
import com.matheusfroes.lolfreeweek.jobs.AndroidJobCreator
import com.matheusfroes.lolfreeweek.jobs.CreateFirstFetchJob


class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(AndroidJobCreator())

        CreateFirstFetchJob.scheduleFirstWeeklyJob()
    }
}