package com.matheusfroes.lolfreeweek.di

import com.matheusfroes.lolfreeweek.di.modules.AppModule
import com.matheusfroes.lolfreeweek.di.modules.JobsModule
import com.matheusfroes.lolfreeweek.di.modules.NetworkModule
import dagger.Component

@Component(modules = [(AppModule::class), (NetworkModule::class), (JobsModule::class)])
interface AppComponent
