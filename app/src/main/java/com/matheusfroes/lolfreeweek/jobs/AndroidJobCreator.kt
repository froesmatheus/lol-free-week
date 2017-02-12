package com.matheusfroes.lolfreeweek.jobs

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

/**
 * Created by matheusfroes on 12/02/2017.
 */
class AndroidJobCreator : JobCreator {
    override fun create(tag: String?): Job? {
        when (tag) {
            CreateFirstFetchJob.TAG -> return CreateFirstFetchJob()
            FetchFreeWeekChampionsJob.TAG -> return FetchFreeWeekChampionsJob()
            else -> return null
        }
    }
}