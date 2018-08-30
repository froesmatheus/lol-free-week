package com.matheusfroes.lolfreeweek.jobs

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import javax.inject.Inject
import javax.inject.Provider

class JobCreator @Inject constructor(
        private val jobs: MutableMap<String, Provider<Job>>
) : JobCreator {

    override fun create(tag: String): Job? = jobs[tag]?.get()

}
