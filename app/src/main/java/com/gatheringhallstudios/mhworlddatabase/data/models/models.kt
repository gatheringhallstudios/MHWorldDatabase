package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

/*
todo: refactor. The current version has problems. Details below:
The current system has several problems:
1) Inconsistently nested sub-data that ends up loading a full query
2) Multiple versions of the same "object". Leads to trouble with icon loading.

It will also need a split like how entities are split.
 */

data class Location(
        val id: Int,
        val name: String?
)

/**
 * Represents an item at a location.
 * It is expected that the location is already known.
 */
data class LocationItem(
        @Embedded(prefix = "item_") val item: ItemBase,
        val rank: Rank?,
        val area: Int,
        val stack: Int,
        val percentage: Int,
        val nodes: Int
)


open class DecorationBase(
        val id: Int,
        val name: String?,
        val slot: Int,
        val icon_color: String?
)

class Decoration(
        id: Int,
        name: String?,
        slot: Int,
        icon_color: String?,

        val rarity: Int,
        @Embedded(prefix = "skill_") val skillTree: SkillTreeBase,
        val mysterious_feystone_chance: Double,
        val glowing_feystone_chance: Double,
        val worn_feystone_chance: Double,
        val warped_feystone_chance: Double
): DecorationBase(id, name, slot, icon_color)


/**
 * A view for basic weapon information.
 * TODO: Replace
 */
data class WeaponBasic(
        var id: Int,
        var name: String?,

        var weapon_type: WeaponType?,
        var rarity: Int,
        var attack: Int,

        var slot_1: Int,
        var slot_2: Int,
        var slot_3: Int
)
