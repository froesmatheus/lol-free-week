package com.matheusfroes.lolfreeweek.di

import com.matheusfroes.lolfreeweek.di.modules.*
import dagger.Component

@Component(modules = [(AppModule::class), (NetworkModule::class), (JobsModule::class), (ViewModelModule::class), (RiotModule::class)])
interface AppComponent
