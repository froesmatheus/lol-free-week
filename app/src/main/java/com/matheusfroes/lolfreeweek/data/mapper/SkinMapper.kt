package com.matheusfroes.lolfreeweek.data.mapper

import com.matheusfroes.lolfreeweek.network.data.SkinResponse
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

        fun map(skinResponse: SkinResponse): com.matheusfroes.lolfreeweek.data.model.Skin {
            return com.matheusfroes.lolfreeweek.data.model.Skin(
                    name = skinResponse.name,
                    num = skinResponse.num,
                    championName = ""
            )
        }

        fun mapCollection(skinsResponse: List<SkinResponse>): List<com.matheusfroes.lolfreeweek.data.model.Skin> {
            return skinsResponse.map { map(it) }
        }
    }
}