package com.matheusfroes.lolfreeweek.data.source

import com.matheusfroes.lolfreeweek.data.mapper.ChampionMapper
import com.matheusfroes.lolfreeweek.data.UserPreferences
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import net.rithms.riot.api.RiotApi
import net.rithms.riot.api.endpoints.champion.dto.Champion
import net.rithms.riot.api.endpoints.static_data.constant.ChampionListTags
import javax.inject.Inject

class ChampionRemoteSource @Inject constructor(
        private val api: RiotApi,
        private val preferences: UserPreferences) {

    suspend fun fetchFreeWeekChampions(): List<Int> = withContext(CommonPool) {
        api.getChampions(preferences.currentPlatform, true).champions.map { it.id }
    }

    suspend fun fetchChampionsData(): List<com.matheusfroes.lolfreeweek.data.model.Champion> = withContext(CommonPool) {
        val response = api.getDataChampionList(
                preferences.currentPlatform,
                null,
                null,
                true,
                ChampionListTags.IMAGE,
                ChampionListTags.SKINS,
                ChampionListTags.SPELLS,
                ChampionListTags.LORE).data

        ChampionMapper.map(response.map { it.value })
    }
}