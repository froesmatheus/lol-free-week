package com.matheusfroes.lolfreeweek.ui.addalerts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.GridLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.matheusfroes.lolfreeweek.BuildConfig
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.extra.Result
import com.matheusfroes.lolfreeweek.extra.appInjector
import com.matheusfroes.lolfreeweek.extra.toast
import com.matheusfroes.lolfreeweek.extra.viewModelProvider
import kotlinx.android.synthetic.main.activity_add_champion_alert.*
import javax.inject.Inject

class AddChampionAlertActivity : AppCompatActivity() {
    private val adapter: ChampionAlertAdapter by lazy { ChampionAlertAdapter() }
    private val interstitialAd by lazy {
        InterstitialAd(applicationContext)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: AddChampionAlertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_champion_alert)
        appInjector.inject(this)

        viewModel = viewModelProvider(viewModelFactory)

        rvChampions.layoutManager = GridLayoutManager(this, 3, GridLayout.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()
        rvChampions.adapter = adapter

        viewModel.getChampions()

        viewModel.champions.observe(this, Observer { result ->
            when (result) {
                is Result.Complete -> {
                    adapter.champions = result.data.toMutableList()
                    rvChampions.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                is Result.InProgress -> {
                    rvChampions.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    rvChampions.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
        })

        etQueryChampionName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.filterChampions(query.toString())
            }
        })


        interstitialAd.adUnitId = BuildConfig.ADMOB_INTERSTITIAL_BANNER_ID

        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                interstitialAd.show()
            }
        }


        viewModel.emptyAlertListEvent.observe(this, Observer {
            toast(getString(R.string.add_at_least_one_champion))
        })


        viewModel.navigateBackEvent.observe(this, Observer {
            toast(getString(R.string.added_alert_success))

            requestNewInterstitial()
            finish()
        })

        btnSaveAlert.setOnClickListener { viewModel.updateChampionAlerts() }
    }

    private fun requestNewInterstitial() {
//        val adRequest = AdRequest.Builder()
//                .addTestDevice("9A0EBA02F3FE24F712EA9B61624675BA")
//                .build()
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }
}
