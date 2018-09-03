package com.matheusfroes.lolfreeweek.di.modules

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.matheusfroes.lolfreeweek.BuildConfig
import dagger.Module
import dagger.Provides
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun okHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .build()
    }

    @Provides
    fun gsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun coroutineCallAdapterFactory(): CoroutineCallAdapterFactory {
        return CoroutineCallAdapterFactory()
    }

}
