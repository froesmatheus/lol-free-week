package com.matheusfroes.lolfreeweek.data.mapper

import com.matheusfroes.lolfreeweek.network.data.SkinResponse

class SkinMapper {

    companion object {
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