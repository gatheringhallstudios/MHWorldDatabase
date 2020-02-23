package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gatheringhallstudios.mhworlddatabase.data.types.ReloadType
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponCategory
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

// note: final is a java keyword which causes problems. Find some way to add it back later

@Entity(tableName = "weapon")
data class WeaponEntity(
        @PrimaryKey
        val id: Int,
        val order_id: Int,
        val weapon_type: WeaponType,
        val category: WeaponCategory,
        val rarity: Int,

        val previous_weapon_id: Int?,
        val create_recipe_id: Int?,
        val upgrade_recipe_id: Int?,

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
        val craftable: Boolean,
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

@Entity(tableName = "weapon_ammo")
data class WeaponAmmoEntity(
        @PrimaryKey
        val id: Int,
        val deviation: String?,
        val special_ammo: String?,
        val normal1_clip: Int,
        val normal1_rapid: Boolean,
        val normal1_recoil: Int,
        val normal1_reload: ReloadType,
        val normal2_clip: Int,
        val normal2_rapid: Boolean,
        val normal2_recoil: Int,
        val normal2_reload: ReloadType,
        val normal3_clip: Int,
        val normal3_rapid: Boolean,
        val normal3_recoil: Int,
        val normal3_reload: ReloadType,
        val pierce1_clip: Int,
        val pierce1_rapid: Boolean,
        val pierce1_recoil: Int,
        val pierce1_reload: ReloadType,
        val pierce2_clip: Int,
        val pierce2_rapid: Boolean,
        val pierce2_recoil: Int,
        val pierce2_reload: ReloadType,
        val pierce3_clip: Int,
        val pierce3_rapid: Boolean,
        val pierce3_recoil: Int,
        val pierce3_reload: ReloadType,
        val spread1_clip: Int,
        val spread1_rapid: Boolean,
        val spread1_recoil: Int,
        val spread1_reload: ReloadType,
        val spread2_clip: Int,
        val spread2_rapid: Boolean,
        val spread2_recoil: Int,
        val spread2_reload: ReloadType,
        val spread3_clip: Int,
        val spread3_rapid: Boolean,
        val spread3_recoil: Int,
        val spread3_reload: ReloadType,
        val sticky1_clip: Int,
        val sticky1_rapid: Boolean,
        val sticky1_recoil: Int,
        val sticky1_reload: ReloadType,
        val sticky2_clip: Int,
        val sticky2_rapid: Boolean,
        val sticky2_recoil: Int,
        val sticky2_reload: ReloadType,
        val sticky3_clip: Int,
        val sticky3_rapid: Boolean,
        val sticky3_recoil: Int,
        val sticky3_reload: ReloadType,
        val cluster1_clip: Int,
        val cluster1_rapid: Boolean,
        val cluster1_recoil: Int,
        val cluster1_reload: ReloadType,
        val cluster2_clip: Int,
        val cluster2_rapid: Boolean,
        val cluster2_recoil: Int,
        val cluster2_reload: ReloadType,
        val cluster3_clip: Int,
        val cluster3_rapid: Boolean,
        val cluster3_recoil: Int,
        val cluster3_reload: ReloadType,
        val recover1_clip: Int,
        val recover1_rapid: Boolean,
        val recover1_recoil: Int,
        val recover1_reload: ReloadType,
        val recover2_clip: Int,
        val recover2_rapid: Boolean,
        val recover2_recoil: Int,
        val recover2_reload: ReloadType,
        val poison1_clip: Int,
        val poison1_rapid: Boolean,
        val poison1_recoil: Int,
        val poison1_reload: ReloadType,
        val poison2_clip: Int,
        val poison2_rapid: Boolean,
        val poison2_recoil: Int,
        val poison2_reload: ReloadType,
        val paralysis1_clip: Int,
        val paralysis1_rapid: Boolean,
        val paralysis1_recoil: Int,
        val paralysis1_reload: ReloadType,
        val paralysis2_clip: Int,
        val paralysis2_rapid: Boolean,
        val paralysis2_recoil: Int,
        val paralysis2_reload: ReloadType,
        val sleep1_clip: Int,
        val sleep1_rapid: Boolean,
        val sleep1_recoil: Int,
        val sleep1_reload: ReloadType,
        val sleep2_clip: Int,
        val sleep2_rapid: Boolean,
        val sleep2_recoil: Int,
        val sleep2_reload: ReloadType,
        val exhaust1_clip: Int,
        val exhaust1_rapid: Boolean,
        val exhaust1_recoil: Int,
        val exhaust1_reload: ReloadType,
        val exhaust2_clip: Int,
        val exhaust2_rapid: Boolean,
        val exhaust2_recoil: Int,
        val exhaust2_reload: ReloadType,
        val flaming_clip: Int,
        val flaming_rapid: Boolean,
        val flaming_recoil: Int,
        val flaming_reload: ReloadType,
        val water_clip: Int,
        val water_rapid: Boolean,
        val water_recoil: Int,
        val water_reload: ReloadType,
        val freeze_clip: Int,
        val freeze_rapid: Boolean,
        val freeze_recoil: Int,
        val freeze_reload: ReloadType,
        val thunder_clip: Int,
        val thunder_rapid: Boolean,
        val thunder_recoil: Int,
        val thunder_reload: ReloadType,
        val dragon_clip: Int,
        val dragon_rapid: Boolean,
        val dragon_recoil: Int,
        val dragon_reload: ReloadType,
        val slicing_clip: Int,
        val slicing_rapid: Boolean,
        val slicing_recoil: Int,
        val slicing_reload: ReloadType,
        val wyvern_clip: Int,
        val wyvern_reload: ReloadType,
        val demon_clip: Int,
        val demon_recoil: Int,
        val demon_reload: ReloadType,
        val armor_clip: Int,
        val armor_recoil: Int,
        val armor_reload: ReloadType,
        val tranq_clip: Int,
        val tranq_recoil: Int,
        val tranq_reload: ReloadType
)

@Entity(tableName = "weapon_melody")
data class WeaponMelodyEntity (
        @PrimaryKey
        val id: Int,
        val notes: String,
        val duration: String,
        val extension: String
)

@Entity(tableName = "weapon_melody_text")
data class WeaponMelodyTextEntity (
        @PrimaryKey
        val id: Int,
        val lang_id: String,
        val effect1: String,
        val effect2: String
)

@Entity(tableName = "weapon_skill",
        primaryKeys = ["weapon_id", "skilltree_id"],
        foreignKeys = [
                (ForeignKey(entity = WeaponEntity::class,
                        parentColumns = ["id"],
                        childColumns = ["weapon_id"])),
                (ForeignKey(entity = SkillTreeEntity::class,
                        parentColumns = ["id"],
                        childColumns = ["skilltree_id"]))
        ])
data class WeaponSkill(
        val weapon_id: Int,
        val skilltree_id: Int,
        val level: Int
)