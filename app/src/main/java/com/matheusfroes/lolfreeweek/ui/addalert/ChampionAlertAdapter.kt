package com.matheusfroes.lolfreeweek.ui.addalert

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.UserPreferences
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.champion_alert_view.view.*


class ChampionAlertAdapter : RecyclerView.Adapter<ChampionAlertAdapter.ViewHolder>() {

    var champions: MutableList<Champion> = mutableListOf()
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
        fun bind(champion: Champion) {
            Picasso
                    .with(itemView.context)
                    .load("http://ddragon.leagueoflegends.com/cdn/${UserPreferences().currentApiVersion}/img/champion/${champion.image}")
                    .fit()
                    .centerCrop()
                    .into(itemView.ivChampion)

            itemView.cbCheckAlert.setOnCheckedChangeListener(null)

            //if true, your checkbox will be selected, else unselected
            itemView.cbCheckAlert.isChecked = champion.alertOn

            itemView.cbCheckAlert.setOnCheckedChangeListener { _, isChecked ->
                val champ = champions[adapterPosition]
                champ.alertOn = isChecked
            }

            itemView.cardChampion.setOnClickListener {
                itemView.cbCheckAlert.isChecked = !itemView.cbCheckAlert.isChecked
            }
        }
    }
}