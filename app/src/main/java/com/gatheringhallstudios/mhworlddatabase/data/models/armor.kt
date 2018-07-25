package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType

/**
 * The base model for a armor containing the basic identifying information
 */
open class ArmorBase(
        val id: Int,
        val name: String?,
        val rarity: Int,
        val armor_type: ArmorType
)

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

        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int,

        val defense_base: Int,
        val defense_max: Int,
        val defense_augment_max: Int,
        val fire: Int,
        val water: Int,
        val thunder: Int,
        val ice: Int,
        val dragon: Int
): ArmorBase(id, name, rarity, armor_type) {
    /**
     * Generates a list containing all slot values,
     * where each value is the "level" of the slot.
     * 0 means that the slot doesn't exist.
     */
    val slots get() = listOf(slot_1, slot_2, slot_3)
}

/**
 * Representation of a single armor set
 */
class ArmorSet(
        val armorset_id: Int,
        val armorset_name: String?,
        val armor: List<Armor>
) {
    val rarity get() = armor.first().rarity
}

/**
 * Basic representation of a single armor set bonus
 */
class ArmorSetBonus(
        @Embedded(prefix="skilltree_") val skillTree: SkillTree,
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
)
