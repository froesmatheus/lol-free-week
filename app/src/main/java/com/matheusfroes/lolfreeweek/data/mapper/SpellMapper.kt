package com.matheusfroes.lolfreeweek.data.mapper

import com.matheusfroes.lolfreeweek.data.model.Spell
import com.matheusfroes.lolfreeweek.network.data.SpellResponse

class SpellMapper {

    companion object {
        fun map(spellResponse: SpellResponse): Spell {
            return Spell(
                    image = spellResponse.image.full,
                    description = spellResponse.description,
                    name = spellResponse.name
            )
        }

        fun mapCollection(spellsResponse: List<SpellResponse>): List<Spell> {
            return spellsResponse.map { map(it) }
        }
    }
}