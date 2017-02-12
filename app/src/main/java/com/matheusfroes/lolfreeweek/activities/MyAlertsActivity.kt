package com.matheusfroes.lolfreeweek.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.adapters.ChampionMyAlertsAdapter
import com.matheusfroes.lolfreeweek.db.ChampionDAO
import com.matheusfroes.lolfreeweek.models.Champion
import kotlinx.android.synthetic.main.activity_my_alerts.*

class MyAlertsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_alerts)


        val championDAO = ChampionDAO(this)

        val championList = championDAO.getChampionsByAlert(alert = true)
        val adapter = ChampionMyAlertsAdapter(this, championList)
        rvChampions.adapter = adapter
        rvChampions.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvChampions.itemAnimator = DefaultItemAnimator()

        checkIfListEmpty(championList)

        adapter.setOnChampionImageClick(object : ChampionMyAlertsAdapter.OnChampionClickListener {
            override fun onClick(view: View, position: Int) {
                championList[position].alertOn = false
                championDAO.update(championList[position])

                championList.removeAt(position)
                adapter.notifyDataSetChanged()
                adapter.notifyItemRemoved(position)
                checkIfListEmpty(championList)
            }
        })
    }

    private fun checkIfListEmpty(championList: List<Champion>) {
        if (championList.isEmpty()) {
            rvChampions.visibility = View.INVISIBLE
            emptyListLayout.visibility = View.VISIBLE
        } else {
            rvChampions.visibility = View.VISIBLE
            emptyListLayout.visibility = View.INVISIBLE
        }
    }
}
