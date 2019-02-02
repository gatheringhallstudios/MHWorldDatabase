package com.gatheringhallstudios.mhworlddatabase.data.models

import androidx.room.Embedded
import androidx.room.Ignore
import com.gatheringhallstudios.mhworlddatabase.common.MHEntity
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

/**
 * An embedded class representing the available slots on a piece of armor.
 * Can be iterated on.
 */
data class ArmorSlots(
        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int
): Iterable<Int> {
    /**
     * Returns a list containing only active (non-zero) slots
     */
    @Ignore val active = this.asSequence().filter { it > 0 }.toList()

    /**
     * Returns true if the armor contains slots, false otherwise
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

/**
 * The base model for a armor containing the basic identifying information
 */
open class ArmorBase(
        val id: Int,
        val name: String?,
        val rarity: Int,
        val armor_type: ArmorType
): MHEntity {
    override val entityId get() = id
    override val entityType get() = DataType.ARMOR

    // note: slots are a var because @Embedded in both child AND parent params would cause issues,
    // and @Embedded only works on val/var fields (and we need to use non-var in the child)

    /**
     * A list of slot level values (0-3) that can be iterated on.
     */
    @Embedded lateinit var slots: ArmorSlots
}

/**
 * Full information regarding an armor model
 */
class Armor(
        id: Int,
        name: String?,
        rarity: Int,
        armor_type: ArmorType,

        val armorset_name: String?,

        val armorset_id: Int,
        val armorset_bonus_id: Int?,
        val male: Boolean,
        val female: Boolean,

        val defense_base: Int,
        val defense_max: Int,
        val defense_augment_max: Int,
        val fire: Int,
        val water: Int,
        val thunder: Int,
        val ice: Int,
        val dragon: Int
): ArmorBase(id, name, rarity, armor_type)

abstract class ArmorSetBase(
        val armorset_id: Int,
        val armorset_name: String?
) {
    abstract val rarity: Int
}

/**
 * Representation of a single armor set
 */
class ArmorSet(
        armorset_id: Int,
        armorset_name: String?,
        val armor: List<Armor>
) : ArmorSetBase(armorset_id, armorset_name) {
    override val rarity get() = armor.first().rarity
}

/**
 * Representation of a single armor set
 */
class ArmorSetFull(
        armorset_id: Int,
        armorset_name: String?,
        val armor: List<ArmorFull>
): ArmorSetBase(armorset_id, armorset_name) {
    override val rarity get() = armor.first().armor.rarity
}

/**
 * Basic representation of a single armor set bonus
 */
class ArmorSetBonus(
        @Embedded(prefix="skilltree_") val skillTree: SkillTreeBase,
        val id: Int,
        val name: String?,
        val required: Int,
        val description: String?
)

/**
 * The result of fully loading a piece of armor.
 * Loads the data you'd find in an armor screen ingame.
 * Armor set bonuses are contained in the object as they're also scoped to armor ingame.
 */
class ArmorFull(
        val armor: Armor,
        val setBonuses: List<ArmorSetBonus>,
        val recipe: List<ItemQuantity>,
        val skills: List<SkillLevel>
): MHEntity {
    override val entityId get() = armor.id
    override val entityType get() = DataType.ARMOR
}
