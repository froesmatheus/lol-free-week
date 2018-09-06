package com.matheusfroes.lolfreeweek.ui.freeweeklist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import androidx.work.*
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.appInjector
import com.matheusfroes.lolfreeweek.extra.viewModelProvider
import com.matheusfroes.lolfreeweek.jobs.FetchFreeWeekChampionsWorker
import com.matheusfroes.lolfreeweek.ui.BaseActivity
import com.matheusfroes.lolfreeweek.ui.addalert.AddChampionAlertActivity
import com.matheusfroes.lolfreeweek.ui.fetchchampiondata.FetchChampionsDataActivity
import com.matheusfroes.lolfreeweek.ui.myalerts.MyAlertsActivity
import com.matheusfroes.lolfreeweek.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class FreeWeekListActivity : BaseActivity() {
    private val adapter: ChampionAdapter by lazy { ChampionAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: FreeWeekListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appInjector.inject(this)

        if (preferences.firstAccess) {
            startActivity(Intent(this, FetchChampionsDataActivity::class.java))
            finish()
            return
        }

        scheduleJobs()

        viewModel = viewModelProvider(viewModelFactory)


        viewModel.freeToPlayChampions.observe(this, Observer { result ->
            when (result) {
                is Result.Complete -> {
                    adapter.champions = result.data

                    rvChampions.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                is Result.InProgress -> {
                    rvChampions.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    rvChampions.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        })


//        // Test ads
//        val adRequest = AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("9A0EBA02F3FE24F712EA9B61624675BA")
//                .build()
//
////        // Official ads
////        val adRequest = AdRequest.Builder().build()
//        adView.loadAd(adRequest)


        rvChampions.layoutManager = GridLayoutManager(this, 2, GridLayout.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()
        rvChampions.adapter = adapter

        btnSaveAlert.setOnClickListener {
            startActivity(Intent(this, AddChampionAlertActivity::class.java))
        }
    }

    private fun scheduleJobs() {

        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val workRequest = PeriodicWorkRequestBuilder<FetchFreeWeekChampionsWorker>(15, TimeUnit.MINUTES, 1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build()


        WorkManager.getInstance()
                .enqueueUniquePeriodicWork("FETCH_FREE_WEEK", ExistingPeriodicWorkPolicy.KEEP, workRequest)

        WorkManager.getInstance().getStatusById(workRequest.id).observe(this, Observer { status ->
            Log.d("WORKMANAGER", status.toString())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.actionShowAlerts -> startActivity(Intent(this, MyAlertsActivity::class.java))
            R.id.actionConfigs -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.actionRefreshList -> viewModel.refreshFreeWeekList()
        }


        return super.onOptionsItemSelected(item)
    }
}
