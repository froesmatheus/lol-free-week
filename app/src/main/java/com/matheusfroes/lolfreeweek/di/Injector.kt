package com.matheusfroes.lolfreeweek.di

import com.matheusfroes.lolfreeweek.activities.*
import com.matheusfroes.lolfreeweek.di.modules.AppModule
import com.matheusfroes.lolfreeweek.di.modules.JobsModule
import com.matheusfroes.lolfreeweek.di.modules.NetworkModule
import com.matheusfroes.lolfreeweek.jobs.FetchFreeWeekChampionsWorker
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, JobsModule::class])
interface Injector {
    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: SettingsActivity)
    fun inject(downloadChampionDataActivity: DownloadChampionDataActivity)
    fun inject(championDetailsActivity: ChampionDetailsActivity)
    fun inject(introActivity: IntroActivity)
    fun inject(baseActivity: BaseActivity)
    fun inject(fetchFreeWeekChampionsWorker: FetchFreeWeekChampionsWorker)
}