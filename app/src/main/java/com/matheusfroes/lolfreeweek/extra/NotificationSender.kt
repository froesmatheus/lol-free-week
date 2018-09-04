package com.matheusfroes.lolfreeweek.extra

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.UserPreferences
import com.matheusfroes.lolfreeweek.ui.freeweeklist.FreeWeekListActivity
import com.matheusfroes.lolfreeweek.data.dao.ChampionDAO
import net.rithms.riot.api.RiotApi
import java.util.*
import javax.inject.Inject

/**
 * Created by matheusfroes on 16/02/2017.
 */
class NotificationSender @Inject constructor(
        private val context: Context,
        private val api: RiotApi,
        private val preferences: UserPreferences) {
    private val dao by lazy { ChampionDAO(context) }
    private val stackBuilder: TaskStackBuilder by lazy { TaskStackBuilder.create(context) }
    private val random by lazy { Random() }

    fun notifyUser() {
        fetchFreeWeekChampions()
    }

    private fun notifyUserFromJob(title: String, message: String) {
        val intent = Intent(context, FreeWeekListActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        sendNotification(random.nextInt(1000), context, title, message, resultPendingIntent)
    }

    private fun fetchFreeWeekChampions() {
        val response = api.getChampions(preferences.currentPlatform, true)
        val currentFreeChampion = dao.getFreeToPlayChampions()[0]

        // Verificando se a free week mudou
        val newChampions = response.champions.all { champion ->
            champion.id != currentFreeChampion.id
        }

        notifyUserFromJob("Testando notificações", "Olá olá olá")


        if (newChampions) {
            val title = context.resources.getString(R.string.app_name)
            val message = context.resources.getString(R.string.free_week_notification_message)

            // notify user with the free champion rotation
            notifyUserFromJob(title, message)

            val championsByAlert = dao.getChampionsByAlert(true)

            response.champions.forEach { champ ->
                championsByAlert.forEach {
                    if (it.id == champ.id) {
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
}