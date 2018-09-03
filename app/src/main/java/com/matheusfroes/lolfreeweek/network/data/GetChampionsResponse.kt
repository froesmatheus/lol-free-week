package com.matheusfroes.lolfreeweek.network.data

data class GetChampionsResponse(
        val type: String,
        val format: String,
        val version: String,
        val data: Map<String, Void>)

