package com.matheusfroes.lolfreeweek.ui.chooseregion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.dto.RegionWithPlatform
import com.matheusfroes.lolfreeweek.data.model.Platform
import com.matheusfroes.lolfreeweek.data.source.UserPreferences
import com.matheusfroes.lolfreeweek.extra.appInjector
import com.matheusfroes.lolfreeweek.ui.fetchchampiondata.FetchChampionsDataActivity
import kotlinx.android.synthetic.main.activity_choose_region.*
import javax.inject.Inject

class ChooseRegionActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: UserPreferences

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ChooseRegionActivity::class.java))
        }
    }

    val adapter by lazy {
        val regions = resources.getStringArray(R.array.lol_regions)
        val platforms = Platform.getPlatforms()

        val items = regions.mapIndexed { index, region -> RegionWithPlatform(region, platforms[index]) }

        return@lazy RegionAdapter(items)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_region)
        appInjector.inject(this)

        rvRegions.adapter = adapter
        rvRegions.layoutManager = LinearLayoutManager(this)

        adapter.onRegionClick = { regionPosition ->
            val platform = when (regionPosition) {
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

            saveUserCurrentPlatform(platform)
            goToFetchChampionDataScreen()
        }
    }

    private fun saveUserCurrentPlatform(platform: Platform) {
        preferences.currentPlatform = platform
    }

    private fun goToFetchChampionDataScreen() {
        FetchChampionsDataActivity.start(this)
        finish()
    }
}
