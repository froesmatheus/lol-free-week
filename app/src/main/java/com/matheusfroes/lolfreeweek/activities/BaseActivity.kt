package com.matheusfroes.lolfreeweek.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chibatching.kotpref.Kotpref
import com.evernote.android.job.JobManager
import com.matheusfroes.lolfreeweek.UserPreferences
import com.matheusfroes.lolfreeweek.appInjector
import com.matheusfroes.lolfreeweek.jobs.JobCreator
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.Platform
import org.jetbrains.anko.doAsync
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var riotApi: RiotApi

    @Inject
    lateinit var jobCreator: JobCreator

    @Inject
    lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appInjector.inject(this)

        JobManager.create(applicationContext).apply {
            addJobCreator(jobCreator)
        }

        Kotpref.init(applicationContext)

        doAsync {
            preferences.currentApiVersion = riotApi.getDataVersions(Platform.BR)[0]
        }
    }
}