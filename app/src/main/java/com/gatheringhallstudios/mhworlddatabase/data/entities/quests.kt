package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestType

/**
 * Entity for quest
 */
@Entity(tableName = "quest")
data class QuestEntity(
        @PrimaryKey val id: Int,
        val order_id: Int,

        val category: QuestCategory,

        val stars: Int,

        val stars_raw: Int,

        val quest_type: QuestType,

        val location_id: Int,

        val zenny: Int
)

/**
 * Translation data for items
 */
@Entity(tableName = "quest_text", primaryKeys = ["id", "lang_id"])
data class QuestText(
        val id: Int,
        val lang_id: String,
        val name: String?,
        val objective: String?,
        val description: String?
)


@Entity(tableName = "quest_monster", primaryKeys = ["quest_id", "monster_id"])
data class QuestMonsterEntity(
        val quest_id: Int,
        val monster_id: Int,
        val quantity: Int,
        val is_objective: Boolean
)

@Entity(tableName = "quest_reward")
data class QuestRewardEntity(
        @PrimaryKey val id: Int,
        val quest_id: Int,
        val group: Char,
        val item_id: Int,
        val stack: Int,
        val percentage: Int
)