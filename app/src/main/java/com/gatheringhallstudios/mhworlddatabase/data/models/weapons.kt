package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Ignore
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.listitem_bow_detail.*

open class WeaponType(
        val name: String,
        val weapon_type: WeaponType
)

class Weapon(
        id: Int,
        name: String,
        weapon_type: WeaponType,
        rarity: Int,
        attack: Int,
        affinity: Int,
        defense: Int?,

        element1: String?,
        element1_attack: Int?,
        element2: String?,
        element2_attack: Int?,
        element_hidden: Boolean,
        previous_weapon_id: Int?,

        val attack_true: Int,
        val craftable: Int,
        val isFinal: Int,
        val kinsect_bonus: KinsectBonus,
        val elderseal: ElderSealLevel,
        val phial: PhialType,
        val phial_power: Int,
        val shelling: ShellingType,
        val shelling_level: Int?,
        val notes: String?,
        @Embedded
        var weaponAmmo: WeaponAmmo? = null

) : WeaponTree(id, name, rarity, weapon_type, attack, affinity, element1, element1_attack, element2, element2_attack,
        element_hidden, defense, previous_weapon_id) {
    @Embedded
    lateinit var weaponCoatings: WeaponCoatings
}

open class WeaponTree(
        val id: Int,
        val name: String,
        val rarity: Int,
        val weapon_type: WeaponType,
        val attack: Int,
        val affinity: Int,

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
 * An embedded class representing the coatings available to bows
 */
data class WeaponCoatings(
        val coating_close: Int?,
        val coating_power: Int?,
        val coating_poison: Int?,
        val coating_paralysis: Int?,
        val coating_sleep: Int?,
        val coating_blast: Int?
) :Iterable<CoatingType> {

    override fun iterator(): Iterator<CoatingType> {
        val buffer = mapOf(Pair(CoatingType.CLOSE_RANGE, coating_close), Pair(CoatingType.POWER, coating_power), Pair(CoatingType.POISON, coating_poison),
                Pair(CoatingType.PARALYSIS, coating_paralysis), Pair(CoatingType.SLEEP, coating_sleep), Pair(CoatingType.BLAST, coating_blast))

        return buffer.filter {(CoatingType, value) -> value != null && value > 0}.map {x -> x.key}.toList().iterator()
    }
}
/**
 * The result of fully loading a weapon.
 * Loads the data you'd find in an weapon screen ingame.
 */
class WeaponFull(
        val weapon: Weapon,
        val recipe: Map<String?, List<ItemQuantity>>
)

data class WeaponAmmo(
        val ammo_id: Int,
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