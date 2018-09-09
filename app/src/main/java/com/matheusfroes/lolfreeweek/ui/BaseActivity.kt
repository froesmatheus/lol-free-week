package com.matheusfroes.lolfreeweek.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chibatching.kotpref.Kotpref
import com.matheusfroes.lolfreeweek.data.source.UserPreferences
import com.matheusfroes.lolfreeweek.extra.appInjector
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appInjector.inject(this)

        Kotpref.init(applicationContext)

//        launch(uiContext) {
//            withContext(networkContext) {
//                preferences.currentApiVersion = riotApi.getDataVersions(Platform.BR)[0]
//            }
//        }
    }
}