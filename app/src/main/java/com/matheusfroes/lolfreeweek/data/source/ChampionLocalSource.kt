package com.matheusfroes.lolfreeweek.data.source

import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import javax.inject.Inject

class ChampionLocalSource @Inject constructor(
        private val championDAO: ChampionDAO) {


    fun getFreeToPlayChampions(): List<Champion> {
        return championDAO.getFreeToPlayChampions()
    }

    fun deleteFreeToPlayChampions() {
        championDAO.deleteFreeChampions()
    }

    fun insertFreeChampions(championIds: List<Int>) {
        championDAO.insertFreeChampions(championIds)
    }

    fun resetFreeToPlayList(champions: List<Int>) {
        deleteFreeToPlayChampions()
        insertFreeChampions(champions)
    }
}