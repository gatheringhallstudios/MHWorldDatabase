package com.gatheringhallstudios.mhworlddatabase.data.models

import androidx.room.Embedded
import androidx.room.Ignore
import com.gatheringhallstudios.mhworlddatabase.data.embeds.MonsterAilments
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryElemental
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryStatus
import com.gatheringhallstudios.mhworlddatabase.data.entities.*
import com.gatheringhallstudios.mhworlddatabase.data.types.*

open class MonsterBase(
        val id: Int,
        val name: String?,
        val size: MonsterSize,
        val ecology: String?
) : MHModel {
    override val entityId get() = id
    override val entityType get() = DataType.MONSTER
}

class Monster(
        id: Int,
        name: String,
        size: MonsterSize,
        ecology: String?,

        val order_id: Int,
        val description: String?,
        val alt_state_description: String?,

        val has_weakness: Boolean,
        val has_alt_weakness: Boolean,

        @Embedded(prefix = "weakness_")
        val weaknesses: WeaknessSummaryElemental?,

        @Embedded(prefix = "weakness_")
        val status_weaknesses: WeaknessSummaryStatus?,

        @Embedded(prefix = "alt_weakness_")
        val alt_weaknesses: WeaknessSummaryElemental?,

        @Embedded(prefix="ailment_")
        val ailments: MonsterAilments?

): MonsterBase(id, name, size, ecology)

class MonsterHabitat(
        @Embedded(prefix = "location_") val location: Location,
        val start_area: String?,
        val move_area: String?,
        val rest_area: String?
) {
    /**
     * A list of all areas a monster moves inbetween
     */
    @Ignore
    val moveAreas = move_area?.split(",")?.map { it.trim() }
}

data class MonsterHitzone(
        @Embedded val data: MonsterHitzoneEntity,
        val body_part: String?
)

data class MonsterBreak(
        val flinch: Int?,
        val wound: Int?,
        val sever: Int?,
        val extract: Extract,
        val part_name: String?
)

/**
 * Represents information for an item rewarded
 * for hunting a monster.
 */
data class MonsterReward(
        val rank: Rank,
        var condition_name: String?,
        val stack: Int,
        val percentage: Int,
        @Embedded(prefix="item_") val item: ItemBase
)
