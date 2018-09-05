package com.matheusfroes.lolfreeweek.network

import com.matheusfroes.lolfreeweek.network.data.GetChampionResponse
import com.matheusfroes.lolfreeweek.network.data.GetChampionRotationResponse
import com.matheusfroes.lolfreeweek.network.data.GetChampionsResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Url

interface RiotService {
    @GET("{apiVersion}/data/{locale}/champion.json")
    fun getChampions(
            @Path("apiVersion") apiVersion: String,
            @Path("locale") locale: String): Deferred<GetChampionsResponse>

    @GET("{apiVersion}/data/{locale}/champion/{championName}.json")
    fun getChampion(
            @Path("apiVersion") apiVersion: String,
            @Path("locale") locale: String,
            @Path("championName") championName: String): Deferred<GetChampionResponse>

    @GET("https://{region}.api.riotgames.com/lol/platform/v3/champion-rotations")
    fun getChampionRotation(
            @Path("region") region: String,
            @Header("X-Riot-Token") riotToken: String): Deferred<GetChampionRotationResponse>
}