package com.matheusfroes.lolfreeweek.di

import com.matheusfroes.lolfreeweek.di.modules.AppModule
import com.matheusfroes.lolfreeweek.di.modules.JobsModule
import com.matheusfroes.lolfreeweek.di.modules.NetworkModule
import com.matheusfroes.lolfreeweek.di.modules.ViewModelModule
import com.matheusfroes.lolfreeweek.jobs.FetchFreeWeekChampionsWorker
import com.matheusfroes.lolfreeweek.ui.BaseActivity
import com.matheusfroes.lolfreeweek.ui.addalert.AddChampionAlertActivity
import com.matheusfroes.lolfreeweek.ui.championdetails.ChampionDetailsActivity
import com.matheusfroes.lolfreeweek.ui.fetchchampiondata.FetchChampionsDataActivity
import com.matheusfroes.lolfreeweek.ui.freeweeklist.FreeWeekList
import com.matheusfroes.lolfreeweek.ui.intro.IntroActivity
import com.matheusfroes.lolfreeweek.ui.settings.SettingsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, JobsModule::class, ViewModelModule::class])
interface Injector {
    fun inject(freeWeekList: FreeWeekList)
    fun inject(mainActivity: SettingsActivity)
    fun inject(fetchChampionsDataActivity: FetchChampionsDataActivity)
    fun inject(championDetailsActivity: ChampionDetailsActivity)
    fun inject(introActivity: IntroActivity)
    fun inject(baseActivity: BaseActivity)
    fun inject(fetchFreeWeekChampionsWorker: FetchFreeWeekChampionsWorker)
    fun inject(addChampionAlertActivity: AddChampionAlertActivity)
}