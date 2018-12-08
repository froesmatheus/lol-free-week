package com.matheusfroes.lolfreeweek.ui.fetchchampiondata

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.data.source.ChampionRemoteSource
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.parallelMap
import com.matheusfroes.lolfreeweek.extra.uiContext
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import javax.inject.Inject

class FetchChampionsDataViewModel @Inject constructor(
        private val localSource: ChampionLocalSource,
        private val remoteSource: ChampionRemoteSource) : ViewModel() {

    private val _downloadChampions = MutableLiveData<Result<Unit>>()

    val downloadChampions: LiveData<Result<Unit>> = _downloadChampions

    fun downloadChampionData() = launch(uiContext) {
        _downloadChampions.value = Result.InProgress()

        try {
            val championNames = remoteSource.getChampions()

            val champions = championNames.parallelMap { championName ->
                remoteSource.getChampion(championName)
            }

            localSource.deleteDatabase()
            localSource.insertChampions(champions)

            val freeWeekChampions = remoteSource.fetchFreeWeekChampions()
            localSource.resetFreeToPlayList(freeWeekChampions)

            _downloadChampions.value = Result.Complete(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            _downloadChampions.value = Result.Error(e)
        }
    }
}