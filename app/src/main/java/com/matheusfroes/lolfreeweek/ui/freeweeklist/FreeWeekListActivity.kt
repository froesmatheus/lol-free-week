package com.matheusfroes.lolfreeweek.ui.freeweeklist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.matheusfroes.lolfreeweek.BuildConfig
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.appInjector
import com.matheusfroes.lolfreeweek.extra.createFreeWeekWorkRequest
import com.matheusfroes.lolfreeweek.extra.viewModelProvider
import com.matheusfroes.lolfreeweek.ui.BaseActivity
import com.matheusfroes.lolfreeweek.ui.addalert.AddChampionAlertActivity
import com.matheusfroes.lolfreeweek.ui.fetchchampiondata.FetchChampionsDataActivity
import com.matheusfroes.lolfreeweek.ui.myalerts.MyAlertsActivity
import com.matheusfroes.lolfreeweek.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
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

        MobileAds.initialize(this, BuildConfig.ADMOB_APP_ID)

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
        val adRequest = AdRequest.Builder()
                .build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adView.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(p0: Int) {
                adView.visibility = View.GONE
            }
        }


        rvChampions.layoutManager = GridLayoutManager(this, 2, GridLayout.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()
        rvChampions.adapter = adapter

        btnSaveAlert.setOnClickListener {
            startActivity(Intent(this, AddChampionAlertActivity::class.java))
        }
    }

    private fun scheduleJobs() {
        val workRequest = createFreeWeekWorkRequest()

        WorkManager.getInstance()
                .beginUniqueWork("FETCH_FW", ExistingWorkPolicy.KEEP, workRequest)
                .enqueue()
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
