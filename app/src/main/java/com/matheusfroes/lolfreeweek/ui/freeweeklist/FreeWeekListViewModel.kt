package com.matheusfroes.lolfreeweek.ui.freeweeklist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.data.source.ChampionRemoteSource
import com.matheusfroes.lolfreeweek.extra.uiContext
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class FreeWeekListViewModel @Inject constructor(
        private val localSource: ChampionLocalSource,
        private val remoteSource: ChampionRemoteSource
) : ViewModel() {
    private val _freeToPlayChampions = MutableLiveData<Result<List<Champion>>>()
    val freeToPlayChampions: LiveData<Result<List<Champion>>> = _freeToPlayChampions

    init {
        getFreeToPlayChampions()
    }

    private fun getFreeToPlayChampions() = launch(uiContext)  {
        _freeToPlayChampions.value = Result.InProgress()
        try {
            val champions = localSource.getFreeToPlayChampions()
            _freeToPlayChampions.value = Result.Complete(champions)
        } catch (e: Exception) {
            _freeToPlayChampions.value = Result.Error(e)

        }
    }

    fun refreshFreeWeekList() = launch(uiContext) {
        _freeToPlayChampions.value = Result.InProgress()

        try {
            val champions = remoteSource.fetchFreeWeekChampions()

            localSource.resetFreeToPlayList(champions)
            getFreeToPlayChampions()
        } catch (e: Exception) {
            _freeToPlayChampions.value = Result.Error(e)
        }
    }
}