package com.gatheringhallstudios.mhworlddatabase.data.views

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

data class ItemView(
        @Embedded val data: ItemEntity,
        val name: String?,
        val description: String?
) {
    val id get() = data.id
}

data class ItemBasicView(
        val id: Int,
        val name: String,
        val icon_name: String?,
        val icon_color: String?
)

data class ItemCombinationView(
        val id: Int,
        @Embedded(prefix = "result_") val result: ItemBasicView,
        @Embedded(prefix = "first_") val first: ItemBasicView,
        @Embedded(prefix = "second_") val second: ItemBasicView?,
        val quantity: Int
)

data class ItemLocationView(
        @Embedded val data: LocationItemEntity,
        val location_name: String
)

data class ArmorView(
        @Embedded val data: ArmorEntity,
        val name: String?,
        val armorset_name: String?
) {
    val id get() = data.id
    val armorset_id get() = data.armorset_id
    val armor_type get() = data.armor_type
    val rarity get() = data.rarity
    val rank get() = data.rank
    val slots get() = data.slots
}

data class ArmorSkillView(
        @Embedded val data: ArmorEntity,
        val name: String?,
        val skillLevel: Int
)

/**
 * Representation of a single armor set
 */
data class ArmorSetView(
        val armorset_id: Int,
        val armorset_name: String?,
        val armor: List<ArmorView>
)

/**
 * Basic representation of a single armor set bonus
 */
data class ArmorSetBonusView(
        val setbonus_id: Int,
        val name:  String?,
        val required: Int,
        val skilltree_id: Int,
        val skillName: String?,
        val description: String?,
        val icon_color: String?
)

data class ArmorComponentView(
        val armor_id: Int,
        val item_id: Int,
        val quantity: Int,
        val item_name: String?
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
        val ecology: String?,
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

data class CharmSkillView(
        @Embedded val data: CharmEntity,
        val name: String?,
        val skillLevel: Int
)

data class SearchResult(
        val data_type: DataType,
        val id: Int,
        val name: String,
        val icon_name: String?,
        val icon_color: String?
)

data class DecorationView(
    val id: Int,
    val name: String?
)

data class DecorationFullView(
        @Embedded val data: DecorationEntity,
        val name: String?
)