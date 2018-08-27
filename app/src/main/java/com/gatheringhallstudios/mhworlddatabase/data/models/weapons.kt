package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Ignore
import com.gatheringhallstudios.mhworlddatabase.data.types.TreeFormatter
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

open class WeaponType(
        val name: String,
        val weapon_type: WeaponType
)

class WeaponTree(
        id: Int,
        name: String,
        rarity: Int,
        weapon_type: String?,
        attack: Int,
        slot_1: Int,
        slot_2: Int,
        slot_3: Int,

        val previous_weapon_id: Int?
) : WeaponBase(id, name, weapon_type, rarity, attack, slot_1, slot_2, slot_3) {

    //Tree related fields
    @Ignore
    var depth: Int? = 0
    @Ignore
    var formatter: MutableList<TreeFormatter> = mutableListOf()
}

/**
 * A view for basic weapon information.
 * TODO: Replace (How?)
 */
open class WeaponBase(
        val id: Int,
        val name: String?,

        val weapon_type: String?,
        val rarity: Int,
        val attack: Int,

        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int
)