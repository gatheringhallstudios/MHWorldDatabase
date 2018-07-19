package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

/*
todo: refactor. The current version has problems. Details below:
The current system has several problems:
1) Inconsistently nested sub-data that ends up loading a full query
2) Multiple versions of the same "object". Leads to trouble with icon loading.

It will also need a split like how entities are split.
 */

data class Item(
        @Embedded val data: ItemEntity,
        val name: String?,
        val description: String?
) {
    val id get() = data.id
}

data class ItemBasic(
        val id: Int,
        val name: String,
        val icon_name: String?,
        val icon_color: String?
)

data class ItemCombination(
        val id: Int,
        @Embedded(prefix = "result_") val result: ItemBasic,
        @Embedded(prefix = "first_") val first: ItemBasic,
        @Embedded(prefix = "second_") val second: ItemBasic?,
        val quantity: Int
)

data class ItemLocation(
        @Embedded val data: LocationItemEntity,
        val location_name: String
)

data class Location(
        val id: Int,
        val name: String?
)

data class LocationItem(
        @Embedded val data: LocationItemEntity,
        val item_name: String?
)

/**
 * Basic representation of a skill tree.
 * This is returned when querying for all data.
 * Created by Carlos on 3/6/2018.
 */
open class SkillTree(
        val id: Int,
        val name: String?,
        val description: String?,
        val icon_color: String?
)

/**
 * A skill tree with skill information included.
 */
class SkillTreeFull(
        id: Int,
        name: String?,
        description: String?,
        icon_color: String?,

        val skills: List<Skill>
) : SkillTree(id, name, description, icon_color)

data class Skill(
        val skilltree_id: Int,
        val level: Int,
        val description: String?
)

data class Monster(
        @Embedded val data: MonsterEntity,
        val name: String?,
        val ecology: String?,
        val description: String?
) {
    val id get() = data.id
}

data class MonsterHabitat(
        @Embedded val data: MonsterHabitatEntity,
        val location_name: String?
)

/**
 * Represents information for a single reward
 */
data class MonsterHitzone(
        @Embedded val data: MonsterHitzoneEntity,
        val body_part: String?
)

data class MonsterBreak(
        @Embedded val data: MonsterBreakEntity,
        val part_name: String?
)

data class MonsterReward(
        @Embedded val data: MonsterRewardEntity,
        var condition_name: String?,
        var item_name: String?
)

data class Charm(
        val id: Int,
        val name: String?,
        val rarity: Int
)

data class CharmSkill(
        @Embedded val data: CharmEntity,
        val name: String?,
        val skillLevel: Int
)

data class Decoration(
        @Embedded val data: DecorationEntity,
        val name: String?
) {
    val id get() = data.id
}


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


data class SearchResult(
        val data_type: DataType,
        val id: Int,
        val name: String,
        val icon_name: String?,
        val icon_color: String?
)