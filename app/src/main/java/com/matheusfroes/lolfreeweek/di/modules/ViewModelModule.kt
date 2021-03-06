package com.matheusfroes.lolfreeweek.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.matheusfroes.lolfreeweek.ui.addalerts.AddChampionAlertViewModel
import com.matheusfroes.lolfreeweek.ui.fetchchampiondata.FetchChampionsDataViewModel
import com.matheusfroes.lolfreeweek.ui.freeweeklist.FreeWeekListViewModel
import com.matheusfroes.lolfreeweek.ui.myalerts.MyAlertsViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FreeWeekListViewModel::class)
    internal abstract fun freeWeekListViewModel(viewModel: FreeWeekListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FetchChampionsDataViewModel::class)
    internal abstract fun fetchChampionsDataViewModel(viewModel: FetchChampionsDataViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddChampionAlertViewModel::class)
    internal abstract fun addChampionAlertViewModel(viewModel: AddChampionAlertViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyAlertsViewModel::class)
    internal abstract fun myAlertsViewModel(viewModel: MyAlertsViewModel): ViewModel
}