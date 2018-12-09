package com.matheusfroes.lolfreeweek.ui.addalerts

import com.matheusfroes.lolfreeweek.data.model.Champion
import com.matheusfroes.lolfreeweek.data.model.ChampionAlert

data class ChampionAlertUIModel(
        val champion: Champion,
        var alert: ChampionAlert?
)