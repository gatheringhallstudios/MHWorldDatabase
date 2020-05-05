package com.gatheringhallstudios.mhworlddatabase.data.models

import androidx.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestType

open class QuestBase(
        val id: Int,
        val category: QuestCategory,
        val stars: Int,
        val stars_raw: Int,
        val name: String,
        val quest_type: QuestType,
        val objective: String?,
        val description: String?,
        val location_id: Int,
        val zenny: Int
) : MHModel {
    override val entityId get() = id
    override val entityType get() = DataType.QUEST
}

data class QuestMonster(
        @Embedded(prefix = "monster_") val monster: MonsterBase,
        val quantity: Int,
        val is_objective: Boolean
)

class QuestReward(
        @Embedded(prefix = "item_") val item: ItemBase,
        val group: String,
        val stack: Int,
        val percentage: Int?
)