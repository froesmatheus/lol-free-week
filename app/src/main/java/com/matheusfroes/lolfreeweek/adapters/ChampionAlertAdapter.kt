package com.matheusfroes.lolfreeweek.adapters

import android.content.Context
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

        Picasso
                .with(context)
                .load("http://ddragon.leagueoflegends.com/cdn/7.3.1/img/champion/${champion.image}")
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