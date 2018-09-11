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
        val deviation: String?,
        val special_ammo: String?,
        val ammo_normal_1: String?,
        val ammo_normal_2: String?,
        val ammo_normal_3: String?,
        val ammo_pierce_1: String?,
        val ammo_pierce_2: String?,
        val ammo_pierce_3: String?,
        val ammo_spread_1: String?,
        val ammo_spread_2: String?,
        val ammo_spread_3: String?,
        val ammo_sticky_1: String?,
        val ammo_sticky_2: String?,
        val ammo_sticky_3: String?,
        val ammo_cluster_1: String?,
        val ammo_cluster_2: String?,
        val ammo_cluster_3: String?,
        val ammo_recover_1: String?,
        val ammo_recover_2: String?,
        val ammo_poison_1: String?,
        val ammo_poison_2: String?,
        val ammo_paralysis_1: String?,
        val ammo_paralysis_2: String?,
        val ammo_sleep_1: String?,
        val ammo_sleep_2: String?,
        val ammo_exhaust_1: String?,
        val ammo_exhaust_2: String?,
        val ammo_flaming: String?,
        val ammo_water: String?,
        val ammo_freeze: String?,
        val ammo_thunder: String?,
        val ammo_dragon: String?,
        val ammo_slicing: String?,
        val ammo_wyvern: String?,
        val ammo_demon: String?,
        val ammo_armor: String?,
        val ammo_tranq: String?,
        val coating_close: String?,
        val coating_power: String?,
        val coating_poison: String?,
        val coating_paralysis: String?,
        val coating_sleep: String?,
        val coating_blast: String?
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