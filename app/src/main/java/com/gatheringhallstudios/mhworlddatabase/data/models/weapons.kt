package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
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
        weapon_type: WeaponType,
        attack: Int,

        val element1: String?,
        val element1_attack: Int?,
        val element2: String?,
        val element2_attack: Int?,
        val element_hidden: Boolean,
        val sharpness: String?,
        val sharpness_maxed: Boolean,
        val defense: Int?,
        val previous_weapon_id: Int?
) : WeaponBase(id, name, weapon_type, rarity, attack) {

    //Tree related fields
    @Ignore
    var depth: Int? = 0
    @Ignore
    var formatter: MutableList<TreeFormatter> = mutableListOf()
    @Embedded
    lateinit var slots: WeaponSlots
}

/**
 * A view for basic weapon information.
 * TODO: Replace (How?)
 */
open class WeaponBase(
        val id: Int,
        val name: String?,

        val weapon_type: WeaponType,
        val rarity: Int,
        val attack: Int
)

/**
 * An embedded class representing the available slots on a wepaon
 * Can be iterated on.
 */
data class WeaponSlots(
        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int
): Iterable<Int> {
    /**
     * Returns a list containing only active (non-zero) slots
     */
    @Ignore val active = this.asSequence().filter { it > 0 }.toList()

    /**
     * Returns true if the weapon contains slots, false otherwise
     */
    @Ignore fun isEmpty() = active.isEmpty()

    override fun iterator(): Iterator<Int> {
        return listOf(slot_1, slot_2, slot_3).iterator()
    }

    operator fun get(i: Int) = when(i) {
        0 -> slot_1
        1 -> slot_2
        2 -> slot_3
        else -> throw IndexOutOfBoundsException("Slot must be from 0-2")
    }
}