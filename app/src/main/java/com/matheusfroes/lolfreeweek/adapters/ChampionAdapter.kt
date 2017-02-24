package com.matheusfroes.lolfreeweek.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.activities.ChampionDetailsActivity
import com.matheusfroes.lolfreeweek.models.Champion
import com.squareup.picasso.Picasso


class ChampionAdapter(val context: Context, val championList: List<Champion>) : RecyclerView.Adapter<ChampionAdapter.ViewHolder>() {
    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.champion_img, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champion = championList[position]

        val ivChampion = holder.itemView.findViewById(R.id.ivChampion) as ImageView
        val tvChampionName = holder.itemView.findViewById(R.id.tvChampionName) as TextView

        val currentApiVersion = preferences.getString("CURRENT_API_VERSION", "7.2.1")


        tvChampionName.text = champion.name
        Picasso
                .with(context)
                .load("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/champion/${champion.image}")
                .fit()
                .centerCrop()
                .into(ivChampion)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChampionDetailsActivity::class.java)

            intent.putExtra("championId", champion.id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = championList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}