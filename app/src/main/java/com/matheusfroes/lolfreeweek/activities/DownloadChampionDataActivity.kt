package com.matheusfroes.lolfreeweek.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.models.Champion
import kotlinx.android.synthetic.main.activity_download_champion_data.*
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.Region
import net.rithms.riot.constant.staticdata.ChampData
import net.rithms.riot.dto.Static.ChampionList
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*

class DownloadChampionDataActivity : AppCompatActivity() {
    val api = RiotApi("RGAPI-0fc93c3d-27bb-4eec-bc2b-f110489aa27d")
    val dao by lazy { ChampionDAO(this) }
    val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_champion_data)

        dialogChooseRegion()
    }

    private fun dialogChooseRegion() {
        val regions = resources.getStringArray(R.array.lol_regions)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, regions)

        val dialog = AlertDialog.Builder(this)
                .setAdapter(adapter) { dialogInterface, which ->
                    val region = when (which) {
                        0 -> Region.NA
                        1 -> Region.LAN
                        2 -> Region.LAS
                        3 -> Region.BR
                        4 -> Region.KR
                        5 -> Region.EUNE
                        6 -> Region.EUW
                        7 -> Region.JP
                        8 -> Region.OCE
                        9 -> Region.RU
                        10 -> Region.TR
                        else -> {
                            Region.GLOBAL
                        }
                    }
                    preferences.edit().putString("REGION", region.name).apply()
                    downloadChampionData(region)
                }
                .setCancelable(false)
                .setTitle(resources.getString(R.string.choose_region))
                .create()

        dialog.show()
    }

    private fun downloadChampionData(region: Region) {
        val current = resources.configuration.locale
        var locale = current.toString()

        progressBar.isIndeterminate = true

        doAsync {
            val languages = api.getDataLanguages(region)
            if (!languages.contains(locale)) {
                locale = Locale.US.toString()
            }
            preferences.edit().putString("LOCALE", locale).apply()

            var response: ChampionList? = null

            try {
                response = api.getDataChampionList(region, locale, null, true, ChampData.IMAGE, ChampData.SKINS, ChampData.SPELLS, ChampData.LORE)
            } catch (e: Exception) {
                toast(getString(R.string.download_failed))
            }

            val champList = response!!.data.map {
                val champ = Champion()
                champ.copyChampion(it.value)
                champ
            }

            dao.insertList(champList)
            downloadCurrentFreeWeek(region)

            uiThread {
                startActivity(Intent(applicationContext, IntroActivity::class.java))
                finish()
            }
        }
    }

    private fun downloadCurrentFreeWeek(region: Region) {
        val freeChamps = api.getFreeToPlayChampions(region)

        val freeChampionsIds = freeChamps.champions.map { it.id }

        dao.deleteFreeChampions()
        dao.insertFreeChampions(freeChampionsIds)
    }
}
