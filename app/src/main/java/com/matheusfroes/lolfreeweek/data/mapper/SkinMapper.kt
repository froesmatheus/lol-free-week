package com.matheusfroes.lolfreeweek.data.mapper

import net.rithms.riot.api.endpoints.static_data.dto.Skin

class SkinMapper {

    companion object {
        fun map(skinResponse: Skin, championName: String): com.matheusfroes.lolfreeweek.data.model.Skin {
            return com.matheusfroes.lolfreeweek.data.model.Skin(
                    name = skinResponse.name,
                    championName = championName,
                    num = skinResponse.num
            )
        }

        fun map(skinsResponse: List<Skin>, championName: String): List<com.matheusfroes.lolfreeweek.data.model.Skin> {
            return skinsResponse.map { map(it, championName) }
        }
    }
}