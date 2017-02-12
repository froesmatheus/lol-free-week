package com.matheusfroes.lolfreeweek.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.GridLayout
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.adapters.ChampionAlertAdapter
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.models.Champion
import kotlinx.android.synthetic.main.activity_add_champion_alert.*
import org.jetbrains.anko.toast

class AddChampionAlertActivity : AppCompatActivity() {
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_champion_alert)

        val championDAO = ChampionDAO(this)

        val championList = championDAO.getChampionsByAlert(alert = false)
        val adapter = ChampionAlertAdapter(context, championList)
        rvChampions.adapter = adapter
        rvChampions.layoutManager = GridLayoutManager(this, 3, GridLayout.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()


        etQueryChampionName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                val filter = championList.filter {
                    it.name.contains(query.toString(), ignoreCase = true)
                }
                adapter.changeDataSource(filter)
            }
        })


        btnSaveAlert.setOnClickListener {
            val alertOnChampions = championList.filter(Champion::alertOn)
            championDAO.updateList(alertOnChampions)

            toast("Alerta adicionado com sucesso")
            finish()
        }
    }
}
