package com.gatheringhallstudios.mhworlddatabase.data.embeds

import com.gatheringhallstudios.mhworlddatabase.data.types.AilmentStrength

/**
 * A collection of attributes regarding a monster's ailments.
 * This data is embedded into the monster as a sub object
 */
class MonsterAilments(
        val roar: AilmentStrength,
        val tremor: AilmentStrength,
        val wind: AilmentStrength,
        val defensedown: Boolean,
        val fireblight: Boolean,
        val waterblight: Boolean,
        val thunderblight: Boolean,
        val iceblight: Boolean,
        val dragonblight: Boolean,
        val blastblight: Boolean,
        val poison: Boolean,
        val sleep: Boolean,
        val paralysis: Boolean,
        val bleed: Boolean,
        val stun: Boolean,
        val mud: Boolean,
        val effluvia: Boolean
)