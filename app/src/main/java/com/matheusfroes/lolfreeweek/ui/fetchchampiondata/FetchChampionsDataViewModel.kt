package com.matheusfroes.lolfreeweek.ui.fetchchampiondata

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.data.source.ChampionRemoteSource
import com.matheusfroes.lolfreeweek.extra.uiContext
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class FetchChampionsDataViewModel @Inject constructor(
        private val localSource: ChampionLocalSource,
        private val remoteSource: ChampionRemoteSource) : ViewModel() {


    private val _fetchChampionData = MutableLiveData<Result<Unit>>()
    val fetchChampionData: LiveData<Result<Unit>> = _fetchChampionData


    fun fetchChampionData() = launch(uiContext) {
        _fetchChampionData.value = Result.InProgress()

        try {
            val champions = remoteSource.fetchChampionsData()
            localSource.insertChampions(champions)

            val freeWeekChampions = remoteSource.fetchFreeWeekChampions()
            localSource.resetFreeToPlayList(freeWeekChampions)

            _fetchChampionData.value = Result.Complete(Unit)
        } catch (e: Exception) {
            _fetchChampionData.value = Result.Error(e)
        }
    }
}