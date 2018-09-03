package com.matheusfroes.lolfreeweek.di.modules

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.matheusfroes.lolfreeweek.BuildConfig
import com.matheusfroes.lolfreeweek.network.RiotService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RiotModule {

    @Provides
    fun retrofit(
            okHttpClient: OkHttpClient,
            coroutineCallAdapterFactory: CoroutineCallAdapterFactory,
            gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(BuildConfig.RIOT_API_BASE_URL)
            .build()

    @Provides
    fun riotService(retrofit: Retrofit): RiotService {
        return retrofit.create(RiotService::class.java)
    }

}
