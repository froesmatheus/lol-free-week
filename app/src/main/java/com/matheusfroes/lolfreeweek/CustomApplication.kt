package com.matheusfroes.lolfreeweek

import android.app.Application
import com.facebook.stetho.Stetho
import com.matheusfroes.lolfreeweek.di.DaggerInjector
import com.matheusfroes.lolfreeweek.di.Injector
import com.matheusfroes.lolfreeweek.di.modules.AppModule
import com.matheusfroes.lolfreeweek.extra.CrashlyticsTree
import timber.log.Timber


open class CustomApplication : Application() {
    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()
        setupDagger()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this);
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }

    private fun setupDagger() {
        injector = DaggerInjector
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}