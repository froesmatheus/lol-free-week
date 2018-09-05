package com.matheusfroes.lolfreeweek.di.modules

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.matheusfroes.lolfreeweek.BuildConfig
import dagger.Module
import dagger.Provides
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
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
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor.Logger { Timber.d(it) }

        return HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun okHttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
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
