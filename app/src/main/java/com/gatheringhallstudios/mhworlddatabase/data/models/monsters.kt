package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryElemental
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryStatus
import com.gatheringhallstudios.mhworlddatabase.data.entities.*
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

open class MonsterBase(
        val id: Int,
        val name: String?,
        val size: MonsterSize
)

class Monster(
        id: Int,
        name: String,
        size: MonsterSize,

        val order_id: Int,
        val ecology: String?,
        val description: String?,

        val has_alt_weakness: Boolean,

        @Embedded(prefix = "weakness_")
        val weaknesses: WeaknessSummaryElemental?,

        @Embedded(prefix = "weakness_")
        val status_weaknesses: WeaknessSummaryStatus?,

        @Embedded(prefix = "alt_weakness_")
        val alt_weaknesses: WeaknessSummaryElemental?
): MonsterBase(id, name, size)

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
        val rank: Rank,
        var condition_name: String?,
        val stack: Int,
        val percentage: Int,
        @Embedded(prefix="item_") val item: ItemBase
)
