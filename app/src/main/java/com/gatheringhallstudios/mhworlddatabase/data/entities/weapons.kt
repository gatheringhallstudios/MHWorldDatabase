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
        val recipe_type: String
)

@Entity(tableName = "weapon_ammo")
data class WeaponAmmoEntity(
        @PrimaryKey
        val id: Int,
        val deviation: String?,
        val special_ammo: String?,
        val normal1_clip: Int,
        val normal1_rapid: Boolean,
        val normal2_clip: Int,
        val normal2_rapid: Boolean,
        val normal3_clip: Int,
        val normal3_rapid: Boolean,
        val pierce1_clip: Int,
        val pierce1_rapid: Boolean,
        val pierce2_clip: Int,
        val pierce2_rapid: Boolean,
        val pierce3_clip: Int,
        val pierce3_rapid: Boolean,
        val spread1_clip: Int,
        val spread1_rapid: Boolean,
        val spread2_clip: Int,
        val spread2_rapid: Boolean,
        val spread3_clip: Int,
        val spread3_rapid: Boolean,
        val sticky1_clip: Int,
        val sticky1_rapid: Boolean,
        val sticky2_clip: Int,
        val sticky2_rapid: Boolean,
        val sticky3_clip: Int,
        val sticky3_rapid: Boolean,
        val cluster1_clip: Int,
        val cluster1_rapid: Boolean,
        val cluster2_clip: Int,
        val cluster2_rapid: Boolean,
        val cluster3_clip: Int,
        val cluster3_rapid: Boolean,
        val recover1_clip: Int,
        val recover1_rapid: Boolean,
        val recover2_clip: Int,
        val recover2_rapid: Boolean,
        val poison1_clip: Int,
        val poison1_rapid: Boolean,
        val poison2_clip: Int,
        val poison2_rapid: Boolean,
        val paralysis1_clip: Int,
        val paralysis1_rapid: Boolean,
        val paralysis2_clip: Int,
        val paralysis2_rapid: Boolean,
        val sleep1_clip: Int,
        val sleep1_rapid: Boolean,
        val sleep2_clip: Int,
        val sleep2_rapid: Boolean,
        val exhaust1_clip: Int,
        val exhaust1_rapid: Boolean,
        val exhaust2_clip: Int,
        val exhaust2_rapid: Boolean,
        val flaming_clip: Int,
        val flaming_rapid: Boolean,
        val water_clip: Int,
        val water_rapid: Boolean,
        val freeze_clip: Int,
        val freeze_rapid: Boolean,
        val thunder_clip: Int,
        val thunder_rapid: Boolean,
        val dragon_clip: Int,
        val dragon_rapid: Boolean,
        val slicing_clip: Int,
        val wyvern_clip: Boolean,
        val demon_clip: Boolean,
        val armor_clip: Boolean,
        val tranq_clip: Boolean
)