package com.matheusfroes.lolfreeweek.data.mapper

import com.matheusfroes.lolfreeweek.network.data.ChampionResponse

class ChampionMapper {

    companion object {
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