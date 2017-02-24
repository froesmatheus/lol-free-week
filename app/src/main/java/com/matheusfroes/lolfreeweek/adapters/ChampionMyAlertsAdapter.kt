package com.matheusfroes.lolfreeweek.adapters

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.models.Champion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.champion_my_alert_view.view.*


class ChampionMyAlertsAdapter(val context: Context, var championList: List<Champion>) :
        RecyclerView.Adapter<ChampionMyAlertsAdapter.ViewHolder>() {
    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    var listener: OnChampionClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.champion_my_alert_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = championList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champion = championList[position]

        val currentApiVersion = preferences.getString("CURRENT_API_VERSION", "7.2.1")

        Picasso
                .with(context)
                .load("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/champion/${champion.image}")
                .fit()
                .centerCrop()
                .into(holder.itemView.ivChampion)

        holder.itemView.tvChampionName.text = champion.name
        holder.itemView.tvChampionTitle.text = champion.title
    }

    interface OnChampionClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setOnChampionImageClick(listener: OnChampionClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.ivDeleteAlert.setOnClickListener {
                listener?.onClick(itemView.ivDeleteAlert, adapterPosition)
            }
        }
    }
}