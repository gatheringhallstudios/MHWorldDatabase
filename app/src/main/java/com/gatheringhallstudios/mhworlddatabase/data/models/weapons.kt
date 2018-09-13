package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Ignore
import com.gatheringhallstudios.mhworlddatabase.data.types.TreeFormatter
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

open class WeaponType(
        val name: String,
        val weapon_type: WeaponType
)

class Weapon(
         id: Int,
         name: String,
         rarity: Int,
         weapon_type: WeaponType,
         attack: Int,

         element1: String?,
         element1_attack: Int?,
         element2: String?,
         element2_attack: Int?,
         element_hidden: Boolean,
         defense: Int?,
         previous_weapon_id: Int?,

         val craftable: Int,
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
) : WeaponTree(id, name, rarity, weapon_type, attack, element1, element1_attack, element2, element2_attack,
        element_hidden, defense, previous_weapon_id)

open class WeaponTree(
        val id: Int,
        val name: String,
        val rarity: Int,
        val weapon_type: WeaponType,
        val attack: Int,

        val element1: String?,
        val element1_attack: Int?,
        val element2: String?,
        val element2_attack: Int?,
        val element_hidden: Boolean,
        val defense: Int?,
        val previous_weapon_id: Int?
) {

    //Tree related fields
    @Ignore
    var depth: Int? = 0
    @Ignore
    var formatter: MutableList<TreeFormatter> = mutableListOf()
    @Embedded
    lateinit var slots: WeaponSlots
    @Embedded
    var sharpnessData: WeaponSharpness? = null
}

/**
 * An embedded class representing the available slots on a weapon
 * Can be iterated on.
 */
data class WeaponSlots(
        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int
) : Iterable<Int> {
    /**
     * Returns a list containing only active (non-zero) slots
     */
    @Ignore
    val active = this.asSequence().filter { it > 0 }.toList()

    /**
     * Returns true if the weapon contains slots, false otherwise
     */
    @Ignore
    fun isEmpty() = active.isEmpty()

    override fun iterator(): Iterator<Int> {
        return listOf(slot_1, slot_2, slot_3).iterator()
    }

    operator fun get(i: Int) = when (i) {
        0 -> slot_1
        1 -> slot_2
        2 -> slot_3
        else -> throw IndexOutOfBoundsException("Slot must be from 0-2")
    }
}

/**
 * An embedded class representing the sharpness on a weapon
 */
data class WeaponSharpness(
        val sharpness_maxed: Boolean?,
        val sharpness: String?
) {
    /**
    Gets the sharpness of the weapon at the provided number of handicraft levels
     */
    fun get(handicraftLevel: Int): IntArray {
        return adjustSharpness(handicraftLevel);
    }

    /**
     * Function to adjust sharpness based on whether or not the weapon is affected by Handicraft
     */
    private fun adjustSharpness(handicraftLevel: Int): IntArray {
        if (handicraftLevel > 5) throw IllegalArgumentException("Handicraft levels only go up to 5!")
        if (sharpness == null || sharpness_maxed == null) {
            return IntArray(0)
        }

        val values = sharpness.split(",").map { it.toInt() }.toIntArray()
        if (sharpness_maxed) return values

        var index = values.size - 1
        val handicraftBonus = 10 * (5 - handicraftLevel)
        var remainder: Int
        do {
            values[index] -= handicraftBonus
            remainder = values[index]
            if (values[index] < 0) {
                values[index] = 0
            }
            index--
        } while (remainder < 0 && index > 0)

        return values
    }
}


/**
 * The result of fully loading a weapon.
 * Loads the data you'd find in an weapon screen ingame.
 */
class WeaponFull(
        val weapon: Weapon,
        val recipe: List<ItemQuantity>
)