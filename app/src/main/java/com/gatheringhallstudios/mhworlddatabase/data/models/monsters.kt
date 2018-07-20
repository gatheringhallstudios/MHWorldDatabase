package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.*
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

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

data class MonsterHitzone(
        @Embedded val data: MonsterHitzoneEntity,
        val body_part: String?
)

data class MonsterBreak(
        @Embedded val data: MonsterBreakEntity,
        val part_name: String?
)

/**
 * Represents information for an item rewarded
 * for hunting a monster.
 */
data class MonsterReward(
        val rank: Rank?,
        var condition_name: String?,
        val stack: Int,
        val percentage: Int,
        @Embedded(prefix="item_") val item: ItemBase
)
