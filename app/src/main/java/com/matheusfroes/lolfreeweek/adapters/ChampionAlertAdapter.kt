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
import kotlinx.android.synthetic.main.champion_alert_view.view.*


class ChampionAlertAdapter(val context: Context, var championList: List<Champion>) :
        RecyclerView.Adapter<ChampionAlertAdapter.ViewHolder>() {

    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.champion_alert_view, parent, false)
        return ViewHolder(view)
    }

    fun changeDataSource(championList: List<Champion>) {
        this.championList = championList
        this.notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champion = championList[position]

        val currentApiVersion = preferences.getString("CURRENT_API_VERSION", "7.2.1")

        Picasso
                .with(context)
                .load("http://ddragon.leagueoflegends.com/cdn/$currentApiVersion/img/champion/${champion.image}")
                .fit()
                .centerCrop()
                .into(holder.itemView.ivChampion)

        holder.itemView.cbCheckAlert.setOnCheckedChangeListener(null)

        //if true, your checkbox will be selected, else unselected
        holder.itemView.cbCheckAlert.isChecked = champion.alertOn

        holder.itemView.cbCheckAlert.setOnCheckedChangeListener { compoundButton, isChecked ->
            champion.alertOn = isChecked
        }


        holder.itemView.cardChampion.setOnClickListener {
            holder.itemView.cbCheckAlert.isChecked = !holder.itemView.cbCheckAlert.isChecked
        }
    }

    override fun getItemCount() = championList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}