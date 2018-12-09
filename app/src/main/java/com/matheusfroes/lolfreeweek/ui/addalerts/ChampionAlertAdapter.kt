package com.matheusfroes.lolfreeweek.ui.addalerts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.model.ChampionAlert
import com.matheusfroes.lolfreeweek.data.source.UserPreferences
import com.matheusfroes.lolfreeweek.extra.loadImage
import kotlinx.android.synthetic.main.champion_alert_view.view.*


class ChampionAlertAdapter : RecyclerView.Adapter<ChampionAlertAdapter.ViewHolder>() {

    var champions: List<ChampionAlertUIModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.champion_alert_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champion = champions[position]

        holder.bind(champion)
    }

    override fun getItemCount() = champions.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: ChampionAlertUIModel) {
            itemView.ivChampion.loadImage("http://ddragon.leagueoflegends.com/cdn/${UserPreferences().currentApiVersion}/img/champion/${model.champion.image}")

            itemView.cbCheckAlert.setOnCheckedChangeListener(null)

            //if true, your checkbox will be selected, else unselected
            itemView.cbCheckAlert.isChecked = model.alert != null

            itemView.cbCheckAlert.setOnCheckedChangeListener { _, isChecked ->
                model.alert = ChampionAlert(model.champion.id)
            }

            itemView.cardChampion.setOnClickListener {
                itemView.cbCheckAlert.isChecked = !itemView.cbCheckAlert.isChecked
            }
        }
    }
}