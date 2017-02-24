package com.matheusfroes.lolfreeweek

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.evernote.android.job.JobManager
import com.matheusfroes.lolfreeweek.jobs.AndroidJobCreator
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.Region
import org.jetbrains.anko.doAsync


class CustomApplication : Application() {
    val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(AndroidJobCreator())


        doAsync {
            val api = RiotApi("RGAPI-0fc93c3d-27bb-4eec-bc2b-f110489aa27d")
            val currentApiVersion = api.getDataVersions(Region.BR)[0]
            preferences.edit().putString("CURRENT_API_VERSION", currentApiVersion).apply()
        }
    }
}