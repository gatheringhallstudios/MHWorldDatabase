package com.gatheringhallstudios.mhworlddatabase.data.models

import androidx.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.common.MHEntity
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
) : MHEntity {
    override val entityId get() = id
    override val entityType get() = DataType.DECORATION
}

class Decoration(
        id: Int,
        name: String?,
        slot: Int,
        icon_color: String?,

        val rarity: Int,
        @Embedded(prefix = "skill_") val skillTree: SkillTreeBase,
        val mysterious_feystone_percent: Double,
        val glowing_feystone_percent: Double,
        val worn_feystone_percent: Double,
        val warped_feystone_percent: Double
) : DecorationBase(id, name, slot, icon_color), MHEntity {
    override val entityId get() = id
    override val entityType get() = DataType.DECORATION
}

class BulkModels(
        val locations: List<Location> = emptyList(),
        val monsters: List<MonsterBase> = emptyList(),
        val skillTrees: List<SkillTree> = emptyList(),
        val charms: List<Charm> = emptyList(),
        val decorations: List<DecorationBase> = emptyList(),
        val armor: List<Armor> = emptyList(),
        val items: List<Item> = emptyList(),
        val weapons: List<Weapon> = emptyList()
) {
    val isEmpty get() = (
            locations.isEmpty() &&
            monsters.isEmpty() &&
            skillTrees.isEmpty() &&
            charms.isEmpty() &&
            decorations.isEmpty() &&
            armor.isEmpty() &&
            items.isEmpty() &&
            weapons.isEmpty())
}

