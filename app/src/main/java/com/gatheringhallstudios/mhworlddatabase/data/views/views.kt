package com.gatheringhallstudios.mhworlddatabase.data.views

import androidx.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

data class ItemView(
        @Embedded val data: ItemEntity,
        val name: String?,
        val description: String?
) {
    val id get() = data.id
}

data class ItemCombinationView(
        @Embedded val result: ItemView,
        @Embedded val first: ItemView,
        @Embedded val second: ItemView,
        val quantity: Int
)

data class ItemLocationView(
        @Embedded val data: LocationItemEntity,
        val location_name: String
)

data class ArmorBasicView(
        val id: Int,
        val name: String?,
        val rarity: Int,
        val rank: Rank,
        val armor_type: ArmorType,
        val armorset_id: Int
)

/**
 * Representation of a single armor set
 */
data class ArmorSetView(
        val armorset_id: Int,
        val armorset_name: String?,

        @Embedded(prefix = "head_")
        val head_armor: ArmorBasicView?,
        @Embedded(prefix = "chest_")
        val chest_armor: ArmorBasicView?,
        @Embedded(prefix = "arms_")
        val arms_armor: ArmorBasicView?,
        @Embedded(prefix = "waist_")
        val waist_armor: ArmorBasicView?,
        @Embedded(prefix = "legs_")
        val legs_armor: ArmorBasicView?
)

data class LocationView(
        val id: Int,
        val name: String?
)

data class LocationItemView(
        @Embedded val data: LocationItemEntity,
        val item_name: String?
)

/**
 * Basic representation of a skill tree.
 * This is returned when querying for all data.
 * Created by Carlos on 3/6/2018.
 */
open class SkillTreeView(
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
) : SkillTreeView(id, name, description, icon_color)

data class Skill(
        val skilltree_id: Int,
        val level: Int,
        val description: String?
)

data class MonsterView(
        @Embedded val data: MonsterEntity,
        val name: String?,
        val description: String?
) {
    val id get() = data.id
}

data class MonsterHabitatView(
        @Embedded val data: MonsterHabitatEntity,
        val location_name: String?
)

/**
 * Represents information for a single reward
 */
data class MonsterHitzoneView(
        @Embedded val data: MonsterHitzoneEntity,
        val body_part: String?
)

data class MonsterBreakView(
        @Embedded val data: MonsterBreakEntity,
        val part_name: String?
)

data class MonsterRewardView(
        @Embedded val data: MonsterRewardEntity,
        var condition_name: String?,
        var item_name: String?
)
