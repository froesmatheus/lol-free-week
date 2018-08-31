package com.matheusfroes.lolfreeweek.data.mapper

import com.matheusfroes.lolfreeweek.data.model.Spell
import net.rithms.riot.api.endpoints.static_data.dto.ChampionSpell

class SpellMapper {

    companion object {
        fun map(spellResponse: ChampionSpell): Spell {
            return Spell(
                    image = spellResponse.image.full,
                    description = spellResponse.description,
                    name = spellResponse.name
            )
        }

        fun map(spellsResponse: List<ChampionSpell>): List<Spell> {
            return spellsResponse.map { map(it) }
        }
    }
}