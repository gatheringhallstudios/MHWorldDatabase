package com.gatheringhallstudios.mhworlddatabase.data.entities

import android.arch.persistence.room.*
import com.gatheringhallstudios.mhworlddatabase.data.types.*


@Entity(tableName = "weapon")
data class WeaponEntity(
        @PrimaryKey val id: Int,
    
        val weapon_type: WeaponType,
        val rarity: Int,
        val attack: Int,
    
        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int
)

@Entity(tableName = "weapon_text", primaryKeys = ["id", "lang_id"])
data class WeaponText(
        val id: Int,
        val lang_id: String,

        val name: String?
)

