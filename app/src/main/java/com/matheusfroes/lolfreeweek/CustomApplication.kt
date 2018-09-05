package com.matheusfroes.lolfreeweek

import android.app.Application
import com.facebook.stetho.Stetho
import com.matheusfroes.lolfreeweek.di.DaggerInjector
import com.matheusfroes.lolfreeweek.di.Injector
import com.matheusfroes.lolfreeweek.di.modules.AppModule
import timber.log.Timber


open class CustomApplication : Application() {
    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()
        setupDagger()
        Stetho.initializeWithDefaults(this)

        Timber.plant(Timber.DebugTree())
    }

    private fun setupDagger() {
        injector = DaggerInjector
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}