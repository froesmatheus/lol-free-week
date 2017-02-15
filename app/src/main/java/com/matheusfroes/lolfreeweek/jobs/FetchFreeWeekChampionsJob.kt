package com.matheusfroes.lolfreeweek.jobs

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.util.Log
import com.evernote.android.job.Job
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.activities.MainActivity
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.sendNotification
import net.rithms.riot.api.RiotApi
import java.util.*

/**
 * Created by matheusfroes on 12/02/2017.
 */
class FetchFreeWeekChampionsJob : Job() {
    val api = RiotApi("RGAPI-0fc93c3d-27bb-4eec-bc2b-f110489aa27d")
    val dao by lazy {
        ChampionDAO(context)
    }
    val stackBuilder: TaskStackBuilder by lazy {
        TaskStackBuilder.create(context)
    }
    val random by lazy {
        Random()
    }


    companion object {
        val TAG = "fetch_free_week_champions_job"
    }

    override fun onRunJob(params: Params?): Result {
        Log.d("LOL-FREE-WEEK", "Fetch Free Week Job just triggered")

        // fetch data from riot api and store in local db
        fetchFreeWeekChampions()

        val title = context.resources.getString(R.string.app_name)
        val message = context.resources.getString(R.string.free_week_notification_message)

        // notify user with the free champion rotation
        notifyUserFromJob(title, message)


        return Result.SUCCESS
    }

    fun notifyUserFromJob(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        sendNotification(random.nextInt(1000), context, title, message, resultPendingIntent)
    }


    fun fetchFreeWeekChampions() {
        val response = api.freeToPlayChampions

        val championsByAlert = dao.getChampionsByAlert(true)

        response.champions.forEach { champ ->
            championsByAlert.forEach {
                if (it.id.toLong() == champ.id) {
                    val championName = dao.getChampion(it.id.toLong())?.name
                    notifyUserFromJob(context.getString(R.string.champion_alert), context.getString(R.string.champion_alert_message, championName))
                }
            }
        }

        val freeChampionsIds = response.champions.map { it.id }

        dao.deleteFreeChampions()
        dao.insertFreeChampions(freeChampionsIds)
    }
}
