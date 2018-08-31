package com.matheusfroes.lolfreeweek.data.model

import net.rithms.riot.api.endpoints.static_data.dto.Champion


data class Champion(var id: Int = 0,
                    var image: String = "",
                    var key: String = "",
                    var lore: String = "",
                    var name: String = "",
                    var title: String = "",
                    var spells: MutableList<Spell> = mutableListOf(),
                    var skins: MutableList<Skin> = mutableListOf(),
                    var alertOn: Boolean = false) {


    fun copyChampion(champion: Champion) {
        this.id = champion.id
        this.image = champion.image.full
        this.key = champion.key
        this.lore = champion.lore
        this.name = champion.name
        this.title = champion.title
        champion.spells.forEach {
            this.spells.add(Spell(image = it.image.full, description = it.description, name = it.name))
        }

        champion.skins.forEach {
            this.skins.add(Skin(name = it.name, num = it.num, championName = this.key))
        }
    }
}

data class Spell(var id: Int = 0,
                 var image: String? = "",
                 var description: String? = "",
                 var name: String? = "")

data class Skin(var name: String = "",
                var num: Int = 0,
                var championName: String = "")