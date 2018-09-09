package com.matheusfroes.lolfreeweek.ui.fetchchampiondata

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.model.Platform
import com.matheusfroes.lolfreeweek.data.source.UserPreferences
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.appInjector
import com.matheusfroes.lolfreeweek.extra.toast
import com.matheusfroes.lolfreeweek.extra.viewModelProvider
import com.matheusfroes.lolfreeweek.ui.intro.IntroActivity
import kotlinx.android.synthetic.main.activity_download_champion_data.*
import timber.log.Timber
import javax.inject.Inject

class FetchChampionsDataActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: FetchChampionsDataViewModel

    @Inject
    lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_champion_data)
        appInjector.inject(this)

        viewModel = viewModelProvider(viewModelFactory)

        dialogChooseRegion()
    }

    private fun dialogChooseRegion() {
        val regions = resources.getStringArray(R.array.lol_regions)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, regions)

        val dialog = AlertDialog.Builder(this)
                .setAdapter(adapter) { _, which ->
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
                    downloadChampions()
                }
                .setCancelable(false)
                .setTitle(resources.getString(R.string.choose_region))
                .create()

        dialog.show()
    }

    private fun downloadChampions() {
        viewModel.downloadChampionData()

        viewModel.downloadChampions.observe(this, android.arch.lifecycle.Observer { result ->
            when (result) {
                is Result.Complete -> {
                    startActivity(Intent(applicationContext, IntroActivity::class.java))
                    finish()
                }
                is Result.InProgress -> {
                    progressBar.isIndeterminate = true
                }
                is Result.Error -> {
                    progressBar.isIndeterminate = false
                    Timber.e(result.error)
                    toast(getString(R.string.download_failed))
                }
            }
        })
    }
}
