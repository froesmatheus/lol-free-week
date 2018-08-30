package com.matheusfroes.lolfreeweek

import android.app.Application
import com.matheusfroes.lolfreeweek.di.DaggerInjector
import com.matheusfroes.lolfreeweek.di.Injector
import com.matheusfroes.lolfreeweek.di.modules.AppModule


open class CustomApplication : Application() {
    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()
        setupDagger()
    }

    private fun setupDagger() {
        injector = DaggerInjector
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}