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

data class MonsterRewardView(
        @Embedded val data: MonsterRewardEntity,
        var condition_name: String?,
        var item_name: String?
)
