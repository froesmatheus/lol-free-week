package com.matheusfroes.lolfreeweek.ui.myalerts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.extra.Result
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class MyAlertsViewModel @Inject constructor(
        private val localSource: ChampionLocalSource
) : ViewModel() {

    init {
        getChampions()
    }

    private var championsList = listOf<Champion>()

    private val _champions = MutableLiveData<Result<List<Champion>>>()
    val champions: LiveData<Result<List<Champion>>> = _champions

    fun getChampions() = launch(UI) {
        _champions.value = Result.InProgress()

        try {
            val champions = localSource.getChampionsByAlert(alert = true)
            championsList = champions
            _champions.value = Result.Complete(champions)
        } catch (e: Exception) {
            _champions.value = Result.Error(e)
        }
    }
}