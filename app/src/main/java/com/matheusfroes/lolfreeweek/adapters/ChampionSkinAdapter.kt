package com.matheusfroes.lolfreeweek.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.models.Skin
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.champion_skin_content.view.*


class ChampionSkinAdapter(val context: Context, val skins: MutableList<Skin>) : RecyclerView.Adapter<ChampionSkinAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skin = skins[position]

        val url = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading/${skin.championName}_${skin.num}.jpg"
        Picasso
                .with(context)
                .load(url)
                .fit()
                .centerCrop()
                .into(holder.itemView.ivChampionSkin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.champion_skin_content, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = skins.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}