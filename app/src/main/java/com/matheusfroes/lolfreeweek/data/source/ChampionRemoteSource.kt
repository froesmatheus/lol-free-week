package com.matheusfroes.lolfreeweek.data.source

import com.matheusfroes.lolfreeweek.data.Platform
import com.matheusfroes.lolfreeweek.data.UserPreferences
import com.matheusfroes.lolfreeweek.data.mapper.ChampionMapper
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.extra.networkContext
import com.matheusfroes.lolfreeweek.network.RiotService
import kotlinx.coroutines.experimental.withContext
import net.rithms.riot.api.RiotApi
import net.rithms.riot.api.endpoints.static_data.constant.ChampionListTags
import javax.inject.Inject

class ChampionRemoteSource @Inject constructor(
        private val api: RiotApi,
        private val preferences: UserPreferences,
        private val riotService: RiotService) {

    suspend fun fetchFreeWeekChampions(): List<Int> = withContext(networkContext) {
        api.getChampions(preferences.currentPlatform, true).champions.map { it.id }
    }

    suspend fun fetchChampionsData(): List<com.matheusfroes.lolfreeweek.data.model.Champion> = withContext(networkContext) {
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

    suspend fun getChampions(): List<String> = withContext(networkContext) {
        riotService.getChampions(preferences.currentApiVersion, Platform.getDefaultLocale()).await()
                .data.map { it.key }
    }


    suspend fun getChampion(championName: String): Champion = withContext(networkContext) {
        val championResponse = riotService.getChampion(preferences.currentApiVersion, Platform.getDefaultLocale(), championName).await().data[championName]
                ?: throw IllegalStateException("Champion response cannot be null")

        ChampionMapper.map(championResponse)
    }
}