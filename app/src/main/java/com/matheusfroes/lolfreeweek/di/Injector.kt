package com.matheusfroes.lolfreeweek.di

import com.matheusfroes.lolfreeweek.di.modules.AppModule
import com.matheusfroes.lolfreeweek.di.modules.NetworkModule
import com.matheusfroes.lolfreeweek.di.modules.RiotModule
import com.matheusfroes.lolfreeweek.di.modules.ViewModelModule
import com.matheusfroes.lolfreeweek.jobs.FetchFreeWeekChampionsWorker
import com.matheusfroes.lolfreeweek.ui.BaseActivity
import com.matheusfroes.lolfreeweek.ui.addalerts.AddChampionAlertActivity
import com.matheusfroes.lolfreeweek.ui.championdetails.ChampionDetailsActivity
import com.matheusfroes.lolfreeweek.ui.chooseregion.ChooseRegionActivity
import com.matheusfroes.lolfreeweek.ui.fetchchampiondata.FetchChampionsDataActivity
import com.matheusfroes.lolfreeweek.ui.freeweeklist.FreeWeekListActivity
import com.matheusfroes.lolfreeweek.ui.intro.IntroActivity
import com.matheusfroes.lolfreeweek.ui.myalerts.MyAlertsActivity
import com.matheusfroes.lolfreeweek.ui.settings.SettingsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class, RiotModule::class])
interface Injector {
    fun inject(freeWeekListActivity: FreeWeekListActivity)
    fun inject(mainActivity: SettingsActivity)
    fun inject(fetchChampionsDataActivity: FetchChampionsDataActivity)
    fun inject(championDetailsActivity: ChampionDetailsActivity)
    fun inject(introActivity: IntroActivity)
    fun inject(baseActivity: BaseActivity)
    fun inject(fetchFreeWeekChampionsWorker: FetchFreeWeekChampionsWorker)
    fun inject(addChampionAlertActivity: AddChampionAlertActivity)
    fun inject(myAlertsActivity: MyAlertsActivity)
    fun inject(chooseRegionActivity: ChooseRegionActivity)
}