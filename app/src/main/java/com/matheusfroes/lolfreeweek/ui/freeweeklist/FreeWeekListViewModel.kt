package com.matheusfroes.lolfreeweek.ui.freeweeklist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.lolfreeweek.Result
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.data.source.ChampionRemoteSource
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class FreeWeekListViewModel @Inject constructor(
        private val localSource: ChampionLocalSource,
        private val remoteSource: ChampionRemoteSource
) : ViewModel() {
    val freeToPlayChampions = MutableLiveData<Result<List<Champion>>>()

    init {
        getFreeToPlayChampions()
    }

    private fun getFreeToPlayChampions() {
        val champions = localSource.getFreeToPlayChampions()
        freeToPlayChampions.value = Result.Complete(champions)
    }

    fun refreshFreeWeekList() = launch(UI) {
        freeToPlayChampions.value = Result.InProgress()

        try {
            val champions = remoteSource.fetchFreeWeekChampions()
            val championsIds = champions.map { it.id }

            localSource.resetFreeToPlayList(championsIds)
            getFreeToPlayChampions()
        } catch (e: Exception) {
            freeToPlayChampions.value = Result.Error(e)
        }
    }
}