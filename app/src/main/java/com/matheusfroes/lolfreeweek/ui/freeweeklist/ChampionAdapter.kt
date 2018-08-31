package com.matheusfroes.lolfreeweek.ui.freeweeklist

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.UserPreferences
import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.ui.championdetails.ChampionDetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.champion_img.view.*


class ChampionAdapter : RecyclerView.Adapter<ChampionAdapter.ViewHolder>() {
    var champions: List<Champion> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.champion_img, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champion = champions[position]

        holder.bind(champion)

    }

    override fun getItemCount() = champions.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(champion: Champion) {
            itemView.tvChampionName.text = champion.name
            Picasso
                    .with(itemView.context)
                    .load("http://ddragon.leagueoflegends.com/cdn/${UserPreferences().currentApiVersion}/img/champion/${champion.image}")
                    .fit()
                    .centerCrop()
                    .into(itemView.ivChampion)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ChampionDetailsActivity::class.java)

                intent.putExtra("championId", champion.id)
                itemView.context.startActivity(intent)
            }
        }
    }
}