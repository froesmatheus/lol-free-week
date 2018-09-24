package com.matheusfroes.lolfreeweek.ui.myalerts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.source.UserPreferences
import com.matheusfroes.lolfreeweek.extra.loadImage
import kotlinx.android.synthetic.main.champion_my_alert_view.view.*


class ChampionMyAlertsAdapter : RecyclerView.Adapter<ChampionMyAlertsAdapter.ViewHolder>() {
    var champions: List<Champion> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var deleteChampionEvent: ((Champion) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.champion_my_alert_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = champions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champion = champions[position]

        holder.bind(champion)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(champion: Champion) {
            itemView.ivChampion.loadImage("http://ddragon.leagueoflegends.com/cdn/${UserPreferences().currentApiVersion}/img/champion/${champion.image}")

            itemView.tvChampionName.text = champion.name
            itemView.tvChampionTitle.text = champion.title
        }

        init {
            itemView.ivDeleteAlert.setOnClickListener {
                deleteChampionEvent?.invoke(champions[adapterPosition])
            }
        }
    }
}