package com.gatheringhallstudios.mhworlddatabase.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank


@Entity(tableName = "armor")
data class ArmorEntity(
        @PrimaryKey val id: Int,

        val rarity: Int,
        val rank: Rank,
        val armor_type: ArmorType,
        val armorset_id: Int,
        val armorset_bonus_id: Int?,
        val male: Boolean,
        val female: Boolean,

        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int,

        val defense_base: Int,
        val defense_max: Int,
        val defense_augment_max: Int,
        val fire: Int,
        val water: Int,
        val thunder: Int,
        val ice: Int,
        val dragon: Int
)

@Entity(tableName = "armor_text",
        primaryKeys = ["id", "lang_id"],
        foreignKeys = [
            (ForeignKey(childColumns = ["id"],
                    parentColumns = ["id"],
                    entity = ArmorEntity::class))
        ])
data class ArmorText(
        val id: Int,
        val lang_id: String,
        val name: String?
)

@Entity(tableName = "armorset")
data class ArmorSet(
        @PrimaryKey val id: Int,
        val rank: Rank,
        val armorset_bonus_id: Int?
)

@Entity(tableName = "armorset_text")
data class ArmorSetTextEntity(
        @PrimaryKey val id: Int,
        val lang_id: String,
        val name: String?
)

@Entity(tableName = "armorset_bonus_skill")
data class ArmorSetBonusEntity(
        @PrimaryKey val setbonus_id: Int,
        val skilltree_id: Int,
        val required: Int
)

@Entity(tableName = "armorset_bonus_text")
data class ArmorSetBonusTextEntity(
        @PrimaryKey val id: Int,
        val lang_id: String,
        val name: String?,
        val description: String?
)

/**
 * Created by Carlos on 3/6/2018.
 */
@Entity(tableName = "armor_skill",
        primaryKeys = ["armor_id", "skilltree_id"],
        foreignKeys = [
            (ForeignKey(entity = ArmorEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["armor_id"])),
            (ForeignKey(entity = SkillTreeEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["skilltree_id"]))
        ])
data class ArmorSkill(
        val armor_id: Int,
        val skilltree_id: Int,
        val level: Int
)

@Entity(tableName = "armor_recipe")
data class ArmorRecipeEntity(
        @PrimaryKey val armor_id: Int,
        val item_id: Int,
        val quantity: Int
)
