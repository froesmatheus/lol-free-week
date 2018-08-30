package com.matheusfroes.lolfreeweek.di.modules

import com.evernote.android.job.Job
import com.matheusfroes.lolfreeweek.jobs.CreateFirstFetchJob
import com.matheusfroes.lolfreeweek.jobs.FetchFreeWeekChampionsJob
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
abstract class JobsModule {

    @Binds
    @IntoMap
    @StringKey(CreateFirstFetchJob.TAG)
    abstract fun createFirstFetchJob(createFirstFetchJob: CreateFirstFetchJob): Job

    @Binds
    @IntoMap
    @StringKey(FetchFreeWeekChampionsJob.TAG)
    abstract fun fetchFreeWeekChampionsJob(fetchFreeWeekChampionsJob: FetchFreeWeekChampionsJob): Job
}
