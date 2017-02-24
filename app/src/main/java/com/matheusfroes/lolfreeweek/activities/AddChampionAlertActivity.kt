package com.matheusfroes.lolfreeweek.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.adapters.ChampionAlertAdapter
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.models.Champion
import kotlinx.android.synthetic.main.activity_add_champion_alert.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class AddChampionAlertActivity : AppCompatActivity() {
    val context = this
    val championDAO by lazy {
        ChampionDAO(this)
    }
    var championList: MutableList<Champion>? = null
    var adapter: ChampionAlertAdapter? = null
    val interstitialAd by lazy {
        InterstitialAd(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_champion_alert)

        rvChampions.layoutManager = GridLayoutManager(this, 3, GridLayout.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()
        rvChampions.visibility = View.INVISIBLE

        doAsync {
            championList = championDAO.getChampionsByAlert(alert = false)

            uiThread {
                adapter = ChampionAlertAdapter(context, championList!!)
                rvChampions.adapter = adapter
                rvChampions.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }




        etQueryChampionName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                val filter = championList?.filter {
                    it.name.contains(query.toString(), ignoreCase = true)
                }
                adapter?.changeDataSource(filter!!)
            }
        })


        interstitialAd.adUnitId = "ca-app-pub-9931312002048408/1857365378"

        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                interstitialAd.show()
            }
        }



        btnSaveAlert.setOnClickListener {
            val alertOnChampions = championList?.filter(Champion::alertOn)

            if (alertOnChampions?.size == 0) {
                toast(getString(R.string.add_at_least_one_champion))
                return@setOnClickListener
            }

            championDAO.updateList(alertOnChampions!!)

            toast(getString(R.string.added_alert_success))

            requestNewInterstitial()
            finish()
        }
    }

    private fun requestNewInterstitial() {
//        val adRequest = AdRequest.Builder()
//                .addTestDevice("9A0EBA02F3FE24F712EA9B61624675BA")
//                .build()
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_alert, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.actionInfo -> {
                showHelpDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showHelpDialog() {
        val dialog = AlertDialog.Builder(this)
                .setMessage(getString(R.string.info_add_alert))
                .create()

        dialog.show()
    }
}
