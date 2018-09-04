package com.matheusfroes.lolfreeweek.network.data

class GetChampionRotationResponse(
        val freeChampionIds: List<Int>,
        val freeChampionIdsForNewPlayers: List<Int>,
        val maxNewPlayerLevel: Int
)