package com.matheusfroes.lolfreeweek.jobs

import com.evernote.android.job.Job
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import net.rithms.riot.api.RiotApi

/**
 * Created by matheusfroes on 12/02/2017.
 */
class FetchFreeWeekChampionsJob : Job() {
    val api = RiotApi("RGAPI-0fc93c3d-27bb-4eec-bc2b-f110489aa27d")
    val dao by lazy {
        ChampionDAO(context)
    }

    companion object {
        val TAG = "fetch_free_week_champions_job"
    }

    override fun onRunJob(params: Params?): Result {
        // fetch data from riot api and store in local db
        fetchFreeWeekChampions()

        return Result.SUCCESS
    }

    fun fetchFreeWeekChampions() {
        val response = api.freeToPlayChampions

        val freeChampionsIds = response.champions.map { it.id }

        dao.deleteFreeChampions()
        dao.insertFreeChampions(freeChampionsIds)
    }
}
