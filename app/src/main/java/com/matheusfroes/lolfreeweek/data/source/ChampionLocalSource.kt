package com.matheusfroes.lolfreeweek.data.source

import com.matheusfroes.lolfreeweek.data.dao.ChampionDAO
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.extra.ioContext
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class ChampionLocalSource @Inject constructor(
        private val championDAO: ChampionDAO) {


    suspend fun getFreeToPlayChampions(): List<Champion> = withContext(ioContext) {
        championDAO.getFreeToPlayChampions()
    }

    private suspend fun deleteFreeToPlayChampions() = withContext(ioContext) {
        championDAO.deleteFreeChampions()
    }

    private suspend fun insertFreeChampions(championIds: List<Int>) = withContext(ioContext) {
        championDAO.insertFreeChampions(championIds)
    }

    suspend fun insertChampions(champions: List<Champion>) = withContext(ioContext) {
        championDAO.insertList(champions)
    }

    suspend fun resetFreeToPlayList(champions: List<Int>) = withContext(ioContext) {
        deleteFreeToPlayChampions()
        insertFreeChampions(champions)
    }

    suspend fun getChampionsByAlert(alert: Boolean): List<Champion> = withContext(ioContext) {
        championDAO.getChampionsByAlert(alert)
    }

    suspend fun championCached(id: Int): Boolean = withContext(ioContext) {
        championDAO.championCached(id)
    }

    fun updateChampionAlerts(champions: List<Champion>) {
        championDAO.updateList(champions)
    }

    suspend fun updateChampion(champion: Champion) = withContext(ioContext) {
        championDAO.update(champion)
    }

    fun deleteDatabase() {
        championDAO.deleteDB()
    }
}