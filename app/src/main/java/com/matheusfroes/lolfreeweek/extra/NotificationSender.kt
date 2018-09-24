package com.matheusfroes.lolfreeweek.extra

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import com.chibatching.kotpref.Kotpref
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.data.source.ChampionRemoteSource
import com.matheusfroes.lolfreeweek.ui.freeweeklist.FreeWeekListActivity
import kotlinx.coroutines.experimental.async
import java.util.*
import javax.inject.Inject

/**
 * Created by matheusfroes on 16/02/2017.
 */
class NotificationSender @Inject constructor(
        private val context: Context,
        private val localSource: ChampionLocalSource,
        private val remoteSource: ChampionRemoteSource) {

    private val stackBuilder: TaskStackBuilder by lazy { TaskStackBuilder.create(context) }
    private val random by lazy { Random() }

    init {
        Kotpref.init(context)
    }

    private fun notifyUserFromJob(title: String, message: String) {
        val intent = Intent(context, FreeWeekListActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        sendNotification(random.nextInt(1000), context, title, message, resultPendingIntent)
    }

    fun fetchFreeWeekChampions() = async {
        val freeWeekChampions = remoteSource.fetchFreeWeekChampions()
        val currentFreeChampion = localSource.getFreeToPlayChampions().firstOrNull()

        // Verificando se a free week mudou
        val newChampions = freeWeekChampions.all { championId ->
            championId != currentFreeChampion?.id
        }

        if (newChampions) {
            val title = context.resources.getString(R.string.app_name)
            val message = context.resources.getString(R.string.free_week_notification_message)

            // notify user with the free champion rotation
            notifyUserFromJob(title, message)

            val championsByAlert = localSource.getChampionsByAlert(true)

            freeWeekChampions.forEach { championId ->
                championsByAlert.forEach { champion ->
                    if (champion.id == championId) {
                        val championName = localSource.getChampion(champion.id.toLong())?.name
                        notifyUserFromJob(context.getString(R.string.champion_alert), context.getString(R.string.champion_alert_message, championName))
                    }
                }
            }

            localSource.resetFreeToPlayList(freeWeekChampions)
        }
    }
}