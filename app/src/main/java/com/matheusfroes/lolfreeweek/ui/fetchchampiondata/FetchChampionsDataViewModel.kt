package com.matheusfroes.lolfreeweek.ui.fetchchampiondata

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.data.source.ChampionRemoteSource
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.ResultDownload
import com.matheusfroes.lolfreeweek.extra.parallelMap
import com.matheusfroes.lolfreeweek.extra.uiContext
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class FetchChampionsDataViewModel @Inject constructor(
        private val localSource: ChampionLocalSource,
        private val remoteSource: ChampionRemoteSource) : ViewModel() {

    private val _downloadChampions = MutableLiveData<ResultDownload<Unit>>()

    val downloadChampions: LiveData<ResultDownload<Unit>> = _downloadChampions

    fun downloadChampionData() = launch(uiContext) {
        _downloadChampions.value = ResultDownload.InProgress(0, 0)

        try {
            val championNames = remoteSource.getChampions()

            val champions = championNames.parallelMap { championName ->
//                _downloadChampions.value = ResultDownload.InProgress(index, championNames.size)
                remoteSource.getChampion(championName)
            }

            localSource.insertChampions(champions)

            val freeWeekChampions = remoteSource.fetchFreeWeekChampions()
            localSource.resetFreeToPlayList(freeWeekChampions)

            _downloadChampions.value = ResultDownload.Complete()
        } catch (e: Exception) {
            _downloadChampions.value = ResultDownload.Error(e)
        }
    }
}