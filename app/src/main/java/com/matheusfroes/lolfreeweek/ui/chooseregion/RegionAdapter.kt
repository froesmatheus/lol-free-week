package com.matheusfroes.lolfreeweek.ui.chooseregion

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.data.dto.RegionWithPlatform
import kotlinx.android.synthetic.main.region_view.view.*

class RegionAdapter(private val items: List<RegionWithPlatform>) : RecyclerView.Adapter<RegionAdapter.ViewHolder>() {

    var onRegionClick: ((Int) -> (Unit))? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.region_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onRegionClick?.invoke(adapterPosition)
            }
        }

        fun bind(item: RegionWithPlatform) {
            itemView.tvRegionName.text = item.region
            itemView.tvRegionInitials.text = item.platform.getName().toUpperCase()
        }
    }
}