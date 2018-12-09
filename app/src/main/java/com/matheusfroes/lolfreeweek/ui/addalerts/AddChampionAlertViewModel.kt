package com.matheusfroes.lolfreeweek.ui.addalerts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.lolfreeweek.data.source.ChampionLocalSource
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.SingleLiveEvent
import com.matheusfroes.lolfreeweek.extra.uiContext
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import javax.inject.Inject

class AddChampionAlertViewModel @Inject constructor(
        private val localSource: ChampionLocalSource
) : ViewModel() {
    private var championsList = listOf<ChampionAlertUIModel>()

    private val _champions = MutableLiveData<Result<List<ChampionAlertUIModel>>>()
    val champions: LiveData<Result<List<ChampionAlertUIModel>>> = _champions

    val emptyAlertListEvent = SingleLiveEvent<Any>()
    val navigateBackEvent = SingleLiveEvent<Any>()

    fun getChampions() = launch(uiContext) {
        _champions.value = Result.InProgress()

        try {
            val champions = localSource.getChampionsByAlert(alert = false)
            championsList = champions.map { ChampionAlertUIModel(it, null) }

            _champions.value = Result.Complete(championsList)
        } catch (e: Exception) {
            Timber.e(e)
            _champions.value = Result.Error(e)
        }
    }

    fun filterChampions(query: String) {
        val filter = championsList.filter { champion ->
            champion.champion.name.contains(query, ignoreCase = true)
        }
        _champions.value = Result.Complete(filter)
    }

    fun updateChampionAlerts() {
        val alertOnChampions = championsList.filter { model -> model.alert != null }

        if (alertOnChampions.isEmpty()) {
            emptyAlertListEvent.call()
            return
        }

        localSource.updateChampionAlerts(alertOnChampions.map { it.champion })
        navigateBackEvent.call()
    }
}