package com.matheusfroes.lolfreeweek.ui.fetchchampiondata

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.matheusfroes.lolfreeweek.R
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
    @Inject
    lateinit var preferences: UserPreferences

    lateinit var viewModel: FetchChampionsDataViewModel
    private var remakeList: Boolean = false

    companion object {
        private const val REMAKE_LIST = "remake_list"

        fun start(context: Context, remakeList: Boolean = false) {
            val intent = Intent(context, FetchChampionsDataActivity::class.java)
            intent.putExtra(REMAKE_LIST, remakeList)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_champion_data)
        appInjector.inject(this)

        viewModel = viewModelProvider(viewModelFactory)
        remakeList = intent.getBooleanExtra(REMAKE_LIST, false)

        btnTryAgain.setOnClickListener {
            downloadChampions()
        }

        downloadChampions()
    }

    private fun downloadChampions() {
        viewModel.downloadChampionData()

        viewModel.downloadChampions.observe(this, android.arch.lifecycle.Observer { result ->
            when (result) {
                is Result.Complete -> {
                    if (!remakeList) {
                        startActivity(Intent(applicationContext, IntroActivity::class.java))
                    }
                    finish()
                }
                is Result.InProgress -> {
                    progressBar.isIndeterminate = true
                }
                is Result.Error -> {
                    progressBar.isIndeterminate = false
                    btnTryAgain.visibility = View.VISIBLE
                    Timber.e(result.error)
                    toast(getString(R.string.download_failed))
                }
            }
        })
    }

    override fun onBackPressed() {
        if (viewModel.downloadChampions.value is Result.InProgress) {
            return
        }
        super.onBackPressed()
    }
}
