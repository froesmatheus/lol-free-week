package com.matheusfroes.lolfreeweek.ui.myalerts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.UserPreferences
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.champion_my_alert_view.view.*


class ChampionMyAlertsAdapter : RecyclerView.Adapter<ChampionMyAlertsAdapter.ViewHolder>() {
    var champions: List<Champion> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var listener: OnChampionClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.champion_my_alert_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = champions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champion = champions[position]

        holder.bind(champion)
    }

    interface OnChampionClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setOnChampionImageClick(listener: OnChampionClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(champion: Champion) {
            Picasso
                    .with(itemView.context)
                    .load("http://ddragon.leagueoflegends.com/cdn/${UserPreferences().currentApiVersion}/img/champion/${champion.image}")
                    .fit()
                    .centerCrop()
                    .into(itemView.ivChampion)

            itemView.tvChampionName.text = champion.name
            itemView.tvChampionTitle.text = champion.title
        }

        init {
            itemView.ivDeleteAlert.setOnClickListener {
                listener?.onClick(itemView.ivDeleteAlert, adapterPosition)
            }
        }
    }
}