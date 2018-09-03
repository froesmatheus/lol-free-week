package com.matheusfroes.lolfreeweek.data.mapper

import com.matheusfroes.lolfreeweek.network.data.ChampionResponse
import net.rithms.riot.api.endpoints.static_data.dto.Champion

class ChampionMapper {

    companion object {
        fun map(championResponse: Champion): com.matheusfroes.lolfreeweek.data.model.Champion {
            return com.matheusfroes.lolfreeweek.data.model.Champion(
                    id = championResponse.id,
                    image = championResponse.image.full,
                    key = championResponse.key,
                    lore = championResponse.lore,
                    title = championResponse.title,
                    spells = SpellMapper.map(championResponse.spells).toMutableList(),
                    skins = SkinMapper.map(championResponse.skins, championResponse.key).toMutableList(),
                    name = championResponse.name)
        }

        fun map(championsResponse: List<Champion>): List<com.matheusfroes.lolfreeweek.data.model.Champion> {
            return championsResponse.map { map(it) }
        }


        fun map(championResponse: ChampionResponse): com.matheusfroes.lolfreeweek.data.model.Champion {
            return com.matheusfroes.lolfreeweek.data.model.Champion(
                    id = championResponse.key,
                    image = championResponse.image.full,
                    key = championResponse.id,
                    lore = championResponse.lore,
                    title = championResponse.title,
                    spells = SpellMapper.mapCollection(championResponse.spells).toMutableList(),
                    skins = SkinMapper.mapCollection(championResponse.skins).toMutableList(),
                    name = championResponse.name)
        }
    }
}