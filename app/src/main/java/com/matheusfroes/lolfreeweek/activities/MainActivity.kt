package com.matheusfroes.lolfreeweek.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.adapters.ChampionAdapter
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.models.Champion
import kotlinx.android.synthetic.main.activity_main.*
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.staticdata.ChampData
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : AppCompatActivity() {
    val context = this
    val dao by lazy { ChampionDAO(this) }
    val api = RiotApi("RGAPI-0fc93c3d-27bb-4eec-bc2b-f110489aa27d")

    val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Check if it's the user first access
        if (preferences.getBoolean("FIRST_ACCESS", true)) {
            startActivity(Intent(this, IntroActivity::class.java))
            preferences.edit().putBoolean("FIRST_ACCESS", false).apply()
        }



        rvChampions.layoutManager = GridLayoutManager(this, 2, GridLayout.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()
        rvChampions.visibility = View.INVISIBLE



        btnSaveAlert.setOnClickListener {
            startActivity(Intent(this, AddChampionAlertActivity::class.java))
        }


        doAsync {
            val response = api.freeToPlayChampions

            downloadChampionsData()

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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.actionShowAlerts -> startActivity(Intent(this, MyAlertsActivity::class.java))
            R.id.actionConfigs -> startActivity(Intent(this, SettingsActivity::class.java))
        }


        return super.onOptionsItemSelected(item)
    }

    fun downloadChampionsData() {
        val current = resources.configuration.locale

        val response = api.getDataChampionList(current.toString(), null, true, ChampData.IMAGE, ChampData.SKINS, ChampData.SPELLS, ChampData.LORE)

        val champList = response.data.map {
            val champ = Champion()
            Log.d("LOL", it.value.spells.toString())
            champ.copyChampion(it.value)
            champ
        }

        dao.insertList(champList)
    }
}
