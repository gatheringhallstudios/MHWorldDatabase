package com.gatheringhallstudios.mhworlddatabase.data.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "weapon")
data class WeaponEntity(
        @PrimaryKey
        val id: Int,
        val weapon_type: String,
        val rarity: Int,
        val attack: Int,
        val attack_true: Int?,
        val affinity: Int,
        val defense: Int,
        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int,
        val element1: String?,
        val element1_attack: Int?,
        val element2: String?,
        val element2_attack: Int?,
        val element_hidden: Int,
        val sharpness: String,
        val sharpness_maxed: Int,
        val previous_weapon_id: Int?,
        val craftable: Int,
        @ColumnInfo(name = "final")
        val isFinal: Int,
        val kinsect_bonus: String?,
        val elderseal: String?,
        val phial: String?,
        val phial_power: Int,
        val shelling: String?,
        val shelling_level: Int?,
        val ammo_id: Int?,
        val coating_close: String?,
        val coating_power: String?,
        val coating_poison: String?,
        val coating_paralysis: String?,
        val coating_sleep: String?,
        val coating_blast: String?,
        val notes: String?
)

@Entity(tableName = "weapon_text", primaryKeys = ["id", "lang_id"])
data class WeaponText(
        val id: Int,
        val lang_id: String,

        val name: String?
)

@Entity(tableName = "weapon_recipe")
data class WeaponRecipe(
        @PrimaryKey val weapon_id: Int,
        val item_id: Int,
        val quantity: Int,
        val recipe_Type: String
)