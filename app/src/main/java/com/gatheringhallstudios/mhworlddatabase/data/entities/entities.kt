package com.gatheringhallstudios.mhworlddatabase.data.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryElemental
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryStatus
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank


/**
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "item")
class ItemEntity(
        @PrimaryKey val id: Int,

        val category: String,
        val rarity: Int,
        val buy_price: Int?,
        val sell_price: Int,
        val carry_limit: Int?
)

/** `
 * Translation data for Item
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "item_text", primaryKeys = ["id", "lang_id"])
data class ItemText(
        val id: Int,
        val lang_id: String,
        val name: String?,
        val description: String?
)

@Entity(tableName = "location_text", primaryKeys = ["id", "lang_id"])
data class LocationText(
        val id: Int,
        val lang_id: String,
        val name: String?
)

@Entity(tableName = "location_item")
data class LocationItemEntity(
        @PrimaryKey val id : Int,
        val location_id : Int,
        val rank : Rank,
        val item_id : Rank,
        val stack : Int,
        val percentage: Int
)

@Entity(tableName = "skilltree")
data class SkillTreeEntity(
        @PrimaryKey val id: Int

        // there is no more data, the rest requires joins
        // if it stays this way, we may remove it and just query on the skilltree_text table instead...
)

/**
 * Translation data for SkillTreeFull
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "skilltree_text",
        primaryKeys = ["id", "lang_id"])
data class SkillTreeText(
        val id: Int,
        val lang_id: String,

        val name: String?,
        val description: String?
)

/**
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "skill",
        primaryKeys = ["skilltree_id", "lang_id", "level"],
        foreignKeys = [
                ForeignKey(
                        entity = SkillTreeEntity::class,
                        parentColumns = ["id"],
                        childColumns = ["skilltree_id"])
        ])
data class SkillEntity(
        var skilltree_id: Int,
        var lang_id: Int,
        var level: Int,
        var description: String?
)


/**
 * Entity for monster
 * Created by Carlos on 3/4/2018.
 */
@Entity(tableName = "monster")
data class MonsterEntity(
        @PrimaryKey val id: Int,

        val size: MonsterSize,

        val has_alt_weakness: Boolean,

        @Embedded(prefix = "weakness_")
        val weaknesses: WeaknessSummaryElemental?,

        @Embedded(prefix = "weakness_")
        val status_weaknesses: WeaknessSummaryStatus?,

        @Embedded(prefix = "alt_weakness_")
        val alt_weaknesses: WeaknessSummaryElemental?,

        val icon: String?
)

/**
 * Entity for Monster translation data
 * Created by Carlos on 3/4/2018.
 */
@Entity(tableName = "monster_text",
        primaryKeys = ["id", "lang_id"])
data class MonsterText(
        var id: Int,
        var lang_id: String,
        var name: String?,
        var description: String?
)

@Entity(tableName = "monster_break")
data class MonsterBreakEntity(
        @PrimaryKey val id: Int,

        val monster_id: Int,

        val flinch: Int?,
        val wound: Int?,
        val sever: Int?,
        val extract: String?
)

@Entity(tableName="monster_break_text",
        primaryKeys = ["id", "lang_id"])
data class MonsterBreakText(
        val id: Int,
        val lang_id: String,
        val part_name: String
)

@Entity(tableName = "monster_habitat")
data class MonsterHabitatEntity(
        @PrimaryKey val id: Int = 0,

        val monster_id: Int,
        val location_id: Int,

        val start_area: String?,
        val move_area: String?,
        val rest_area: String?
)

@Entity(tableName = "monster_hitzone")
data class MonsterHitzoneEntity(
        @PrimaryKey val id: Int,

        val monster_id: Int,

        val cut: Int,
        val impact: Int,
        val shot: Int,

        val fire: Int,
        val water: Int,
        val ice: Int,
        val thunder: Int,
        val dragon: Int,

        val ko: Int
)

@Entity(tableName = "monster_hitzone_text",
        primaryKeys = ["id", "lang_id"])
data class MonsterHitzoneText(
        val id: Int,
        val lang_id: String,
        val name: String?
)

/**
 * MonsterView reward entity. Note that because rewards can have duplicate
 * entries, the monster/condition is not the primary key.
 */
@Entity(tableName = "monster_reward")
data class MonsterRewardEntity(
        @PrimaryKey val id: Int,

        val monster_id: Int,

        val condition_id: Int,

        val rank: Rank?,

        val item_id: Int,
        val stack: Int,
        val percentage: Int
)

@Entity(tableName = "monster_reward_condition_text",
        primaryKeys = ["id", "lang_id"])
data class MonsterRewardConditionText(
        val id: Int,
        val lang_id: String,
        val name: String?
)
