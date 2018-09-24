package com.matheusfroes.lolfreeweek.data.source

import com.matheusfroes.lolfreeweek.BuildConfig
import com.matheusfroes.lolfreeweek.data.mapper.ChampionMapper
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.model.Platform
import com.matheusfroes.lolfreeweek.extra.networkContext
import com.matheusfroes.lolfreeweek.network.RiotService
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class ChampionRemoteSource @Inject constructor(
        private val preferences: UserPreferences,
        private val riotService: RiotService) {

    suspend fun fetchFreeWeekChampions(): List<Int> = withContext(networkContext) {
        riotService.getChampionRotation(preferences.currentPlatform.id, BuildConfig.RIOT_API_KEY).await().freeChampionIds
    }

    suspend fun getChampions(): List<String> = withContext(networkContext) {
        val lastestApiVersion = getLatestApiVersion()

        return@withContext riotService.getChampions(lastestApiVersion, Platform.getDefaultLocale()).await()
                .data.map { it.key }
    }

    suspend fun getChampion(championName: String): Champion = withContext(networkContext) {
        val championResponse = riotService.getChampion(preferences.currentApiVersion, Platform.getDefaultLocale(), championName).await().data[championName]
                ?: throw IllegalStateException("Champion response cannot be null")

        return@withContext ChampionMapper.map(championResponse)
    }

    private suspend fun getLatestApiVersion(): String = withContext(networkContext) {
        val apiVersions = riotService.getLatestApiVersion().await()
        preferences.currentApiVersion = apiVersions.first()
        return@withContext preferences.currentApiVersion
    }
}