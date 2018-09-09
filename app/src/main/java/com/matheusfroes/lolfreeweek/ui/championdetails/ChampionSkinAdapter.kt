package com.matheusfroes.lolfreeweek.ui.championdetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.dto.SkinWithChampionName
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.champion_skin_content.view.*


class ChampionSkinAdapter : RecyclerView.Adapter<ChampionSkinAdapter.ViewHolder>() {
    var skins = listOf<SkinWithChampionName>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skin = skins[position]

        holder.bind(skin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.champion_skin_content, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = skins.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(skin: SkinWithChampionName) {
            val url = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading/${skin.championName}_${skin.skin.num}.jpg"
            Picasso
                    .with(itemView.context)
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(itemView.ivChampionSkin)
        }
    }
}