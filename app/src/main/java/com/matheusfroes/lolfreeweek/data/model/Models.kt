package com.matheusfroes.lolfreeweek.data.model


data class Champion(
        var id: Int = 0,
        var image: String = "",
        var key: String = "",
        var lore: String = "",
        var name: String = "",
        var title: String = "",
        var spells: List<Spell> = listOf(),
        var skins: List<Skin> = listOf(),
        var alertOn: Boolean = false)

data class Spell(
        var id: Int = 0,
        var image: String? = "",
        var description: String? = "",
        var name: String? = "")

data class Skin(
        var name: String = "",
        var num: Int = 0,
        var championName: String = "")