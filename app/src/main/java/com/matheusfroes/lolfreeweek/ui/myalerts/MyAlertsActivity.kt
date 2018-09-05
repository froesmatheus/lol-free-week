package com.matheusfroes.lolfreeweek.ui.myalerts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.dao.ChampionDAO
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.appInjector
import com.matheusfroes.lolfreeweek.extra.viewModelProvider
import kotlinx.android.synthetic.main.activity_my_alerts.*
import timber.log.Timber
import javax.inject.Inject

class MyAlertsActivity : AppCompatActivity() {
    val adapter: ChampionMyAlertsAdapter by lazy { ChampionMyAlertsAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MyAlertsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_alerts)
        appInjector.inject(this)

        viewModel = viewModelProvider(viewModelFactory)

        viewModel.champions.observe(this, Observer { result ->
            when (result) {
                is Result.Complete -> {
                    adapter.champions = result.data
                    checkIfListEmpty(result.data)
                    hideProgressBar()
                }
                is Result.InProgress -> {
                    showProgressBar()
                }
                is Result.Error -> {
                    hideProgressBar()
                    Timber.e(result.error)
                }
            }
        })

        rvChampions.adapter = adapter
        rvChampions.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()

        adapter.deleteChampionEvent = { champion ->
            viewModel.deleteAlert(champion)
        }
    }

    private fun checkIfListEmpty(championList: List<Champion>) {
        if (championList.isEmpty()) {
            rvChampions.visibility = View.INVISIBLE
            emptyListLayout.visibility = View.VISIBLE
        } else {
            rvChampions.visibility = View.VISIBLE
            emptyListLayout.visibility = View.INVISIBLE
        }
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
