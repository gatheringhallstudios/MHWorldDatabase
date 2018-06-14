package com.gatheringhallstudios.mhworlddatabase.data.views

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.*

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

data class LocationView(
        val id: Int,
        val name: String?
)

data class LocationItemView(
        @Embedded val data: LocationItemEntity,
        val location_name: String?,
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
