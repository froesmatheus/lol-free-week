package com.matheusfroes.lolfreeweek.di

import com.matheusfroes.lolfreeweek.di.modules.AppModule
import com.matheusfroes.lolfreeweek.di.modules.NetworkModule
import com.matheusfroes.lolfreeweek.di.modules.RiotModule
import com.matheusfroes.lolfreeweek.di.modules.ViewModelModule
import dagger.Component

@Component(modules = [(AppModule::class), (NetworkModule::class), (ViewModelModule::class), (RiotModule::class)])
interface AppComponent
