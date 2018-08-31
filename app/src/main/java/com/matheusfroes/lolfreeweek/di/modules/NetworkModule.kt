package com.matheusfroes.lolfreeweek.di.modules

import com.matheusfroes.lolfreeweek.BuildConfig
import dagger.Module
import dagger.Provides
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import java.util.logging.Level
import javax.inject.Singleton

@Module()
class NetworkModule {

    @Provides
    @Singleton
    fun riotApi(apiConfig: ApiConfig): RiotApi {
        return RiotApi(apiConfig)
    }

    @Provides
    @Singleton
    fun riotApiConfig(): ApiConfig {
        return ApiConfig()
                .setKey(BuildConfig.RIOT_API_KEY)
                .setDebugLevel(Level.ALL)
    }
}
