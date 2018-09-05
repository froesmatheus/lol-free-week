package com.matheusfroes.lolfreeweek.data.dto

import com.matheusfroes.lolfreeweek.data.model.Skin

data class SkinWithChampionName(
        val skin: Skin,
        val championName: String
)