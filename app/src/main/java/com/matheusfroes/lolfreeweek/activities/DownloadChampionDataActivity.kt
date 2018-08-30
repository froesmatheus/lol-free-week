package com.matheusfroes.lolfreeweek.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.UserPreferences
import com.matheusfroes.lolfreeweek.appInjector
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.models.Champion
import kotlinx.android.synthetic.main.activity_download_champion_data.*
import net.rithms.riot.api.RiotApi
import net.rithms.riot.api.endpoints.static_data.constant.ChampionListTags
import net.rithms.riot.api.endpoints.static_data.dto.ChampionList
import net.rithms.riot.constant.Platform
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*
import javax.inject.Inject

class DownloadChampionDataActivity : AppCompatActivity() {

    @Inject
    lateinit var api: RiotApi

    @Inject
    lateinit var preferences: UserPreferences
    val dao by lazy { ChampionDAO(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_champion_data)
        appInjector.inject(this)

        dialogChooseRegion()
    }

    private fun dialogChooseRegion() {
        val regions = resources.getStringArray(R.array.lol_regions)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, regions)

        val dialog = AlertDialog.Builder(this)
                .setAdapter(adapter) { dialogInterface, which ->
                    val platform = when (which) {
                        0 -> Platform.NA
                        1 -> Platform.LAN
                        2 -> Platform.LAS
                        3 -> Platform.BR
                        4 -> Platform.KR
                        5 -> Platform.EUNE
                        6 -> Platform.EUW
                        7 -> Platform.JP
                        8 -> Platform.OCE
                        9 -> Platform.RU
                        10 -> Platform.TR
                        else -> {
                            Platform.NA
                        }
                    }
                    preferences.currentPlatform = platform
                    downloadChampionData(platform)
                }
                .setCancelable(false)
                .setTitle(resources.getString(R.string.choose_region))
                .create()

        dialog.show()
    }

    private fun downloadChampionData(region: Platform) {
        val current = resources.configuration.locale
        var locale = current.toString()

        progressBar.isIndeterminate = true

        doAsync {
            val languages = api.getDataLanguages(region)
            if (!languages.contains(locale)) {
                locale = Locale.US.toString()
            }
            var response: ChampionList? = null

            try {
                response = api.getDataChampionList(region, net.rithms.riot.api.endpoints.static_data.constant.Locale.PT_BR, null, true, ChampionListTags.IMAGE, ChampionListTags.SKINS, ChampionListTags.SPELLS, ChampionListTags.LORE)
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

    private fun downloadCurrentFreeWeek(region: Platform) {
        val freeChamps = api.getChampions(region, true)

        val freeChampionsIds = freeChamps.champions.map { it.id }

        dao.deleteFreeChampions()
        dao.insertFreeChampions(freeChampionsIds)
    }
}
