package com.matheusfroes.lolfreeweek.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import com.google.android.gms.ads.AdRequest
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.adapters.ChampionAdapter
import com.matheusfroes.lolfreeweek.appInjector
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.jobs.FetchFreeWeekChampionsJob
import kotlinx.android.synthetic.main.activity_main.*
import net.rithms.riot.api.RiotApi
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


class MainActivity : BaseActivity() {
    val context = this
    val dao by lazy { ChampionDAO(this) }
    @Inject
    lateinit var api: RiotApi

//    @Inject
//    lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appInjector.inject(this)

        FetchFreeWeekChampionsJob.scheduleFreeWeekJob()

        // Test ads
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("9A0EBA02F3FE24F712EA9B61624675BA")
                .build()

//        // Official ads
//        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)



        rvChampions.layoutManager = GridLayoutManager(this, 2, GridLayout.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()
        rvChampions.visibility = View.INVISIBLE


        btnSaveAlert.setOnClickListener {
            startActivity(Intent(this, AddChampionAlertActivity::class.java))
        }


        // Check if it's the user first access
        if (preferences.firstAccess) {
//            CreateFirstFetchJob.scheduleFirstWeeklyJob()
            startActivity(Intent(this, DownloadChampionDataActivity::class.java))
            return
        } else {
            val adapter = ChampionAdapter(context, dao.getFreeToPlayChampions())
            rvChampions.adapter = adapter
            rvChampions.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.actionShowAlerts -> startActivity(Intent(this, MyAlertsActivity::class.java))
            R.id.actionConfigs -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.actionRefreshList -> refreshFreeWeekList()
        }


        return super.onOptionsItemSelected(item)
    }

    private fun refreshFreeWeekList() {
        rvChampions.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        doAsync {
            val region = preferences.currentPlatform

            val response = api.getChampions(region, true)

            val freeChampionsIds = response.champions.map { it.id }

            dao.deleteFreeChampions()
            dao.insertFreeChampions(freeChampionsIds)

            uiThread {
                val adapter = ChampionAdapter(context, dao.getFreeToPlayChampions())
                rvChampions.adapter = adapter
                rvChampions.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
}
