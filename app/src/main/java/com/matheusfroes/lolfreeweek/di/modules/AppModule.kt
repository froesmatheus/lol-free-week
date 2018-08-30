package com.matheusfroes.lolfreeweek.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(val app: Context) {

    @Provides
    @Singleton
    fun provideApp(): Context = app
}