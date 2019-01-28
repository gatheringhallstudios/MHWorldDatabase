package com.gatheringhallstudios.mhworlddatabase.data.models

import androidx.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.common.Bookmarkable
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

/*
todo: refactor. The current version has problems. Details below:
The current system has several problems:
1) Inconsistently nested sub-data that ends up loading a full query
2) Multiple versions of the same "object". Leads to trouble with icon loading.

It will also need a split like how entities are split.
 */


open class DecorationBase(
        val id: Int,
        val name: String?,
        val slot: Int,
        val icon_color: String?
) :Bookmarkable {
    override fun getEntityId(): Int {
        return id
    }

    override fun getType(): DataType {
        return DataType.DECORATION
    }
}

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
) : DecorationBase(id, name, slot, icon_color), Bookmarkable {
    override fun getEntityId(): Int {
        return id
    }

    override fun getType(): DataType {
        return DataType.DECORATION
    }
}

