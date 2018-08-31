package com.matheusfroes.lolfreeweek.ui.addalert

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.SingleLiveEvent
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.extra.uiContext
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class AddChampionAlertViewModel @Inject constructor(
        private val localSource: ChampionLocalSource
) : ViewModel() {

    init {
        getChampions()
    }

    private var championsList = listOf<Champion>()

    private val _champions = MutableLiveData<Result<List<Champion>>>()
    val champions: LiveData<Result<List<Champion>>> = _champions

    val emptyAlertListEvent = SingleLiveEvent<Any>()
    val navigateBackEvent = SingleLiveEvent<Any>()

    fun getChampions() = launch(uiContext) {
        _champions.value = Result.InProgress()

        try {
            val champions = localSource.getChampionsByAlert(alert = false)
            championsList = champions

            _champions.value = Result.Complete(champions)
        } catch (e: Exception) {
            _champions.value = Result.Error(e)
        }
    }

    fun filterChampions(query: String) {
        val filter = championsList.filter {champion ->
            champion.name.contains(query, ignoreCase = true)
        }
        _champions.value = Result.Complete(filter)
    }

    fun updateChampionAlerts() {
        val alertOnChampions = championsList.filter(Champion::alertOn)

        if (alertOnChampions.isEmpty()) {
            emptyAlertListEvent.call()
            return
        }

        localSource.updateChampionAlerts(alertOnChampions)
        navigateBackEvent.call()

    }

}