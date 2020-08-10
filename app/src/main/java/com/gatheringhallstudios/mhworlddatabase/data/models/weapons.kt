package com.gatheringhallstudios.mhworlddatabase.data.models

import androidx.room.Embedded
import androidx.room.Ignore
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * The base class for all weapon model types. Contains the basic identifying information
 */
open class WeaponBase(
        val id: Int,
        val name: String,
        val rarity: Int,
        val weapon_type: WeaponType,
        val category: WeaponCategory,
        val previous_weapon_id: Int?
) : MHParentedModel {
    override val entityId get() = id
    override val entityType get() = DataType.WEAPON
    override val parentId get() = previous_weapon_id
}

/**
 * Helper to calculate max element value.
 * TODO: Once we gain a bunch of calculations, move this somewhere maybe.
 * Perhaps use an extension instead of putting on the weapon
 */
fun calculateMaxElement(elementVal: Int): Int {
    return BigDecimal(elementVal / 10 * 1.3)
            .setScale(0, RoundingMode.HALF_DOWN)
            .toInt() * 10
}

class Weapon(
        id: Int,
        name: String,
        rarity: Int,
        weapon_type: WeaponType,
        category: WeaponCategory,
        previous_weapon_id: Int?,

        val attack: Int,
        val attack_true: Int,
        val affinity: Int,

        val element1: ElementStatus?,
        val element1_attack: Int?,
        val element2: ElementStatus?,
        val element2_attack: Int?,
        val element_hidden: Boolean,
        val defense: Int,

        val craftable: Boolean,
        val kinsect_bonus: KinsectBonus,
        val elderseal: ElderSealLevel,
        val phial: PhialType,
        val phial_power: Int,
        val shelling: ShellingType,
        val shelling_level: Int?,
        val notes: String?,
        val special_ammo: SpecialAmmoType?

) : WeaponBase(id, name, rarity, weapon_type, category, previous_weapon_id) {
    @Embedded
    lateinit var slots: EquipmentSlots

    @Embedded
    var weaponCoatings: WeaponCoatings? = null

    @Embedded
    var sharpnessData: WeaponSharpness? = null

    /**
     * Return the max possible value for element 1 attack power
     */
    val element1_attack_max get() = calculateMaxElement(element1_attack ?: 0)

    /**
     * Return the max possible value for element 2 attack power
     */
    val element2_attack_max get() = calculateMaxElement(element2_attack ?: 0)
}

/**
 * An embedded class representing the available slots on a weapon
 * Can be iterated on.
 */
data class EquipmentSlots(
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
     * Returns the min sharpness level (equivalent to handicraft 0)
     */
    val min get() = get(0)

    /**
     * Returns the max sharpness level (equivalent to handicraft 5)
     */
    val max get() = get(5)

    /**
    Gets the sharpness of the weapon at the provided number of handicraft levels
     */
    fun get(handicraftLevel: Int): IntArray {
        return adjustSharpness(handicraftLevel)
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

        val handicraftReduction = 10 * (5 - handicraftLevel)

        var index = values.size - 1
        var remainder: Int = handicraftReduction
        do {
            val toRemove = Math.min(values[index], remainder)
            values[index] -= toRemove
            remainder -= toRemove
            index--
        } while (remainder > 0 && index > 0)

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
) : Iterable<CoatingType> {

    override fun iterator(): Iterator<CoatingType> {
        val buffer = mapOf(Pair(CoatingType.CLOSE_RANGE, coating_close), Pair(CoatingType.POWER, coating_power), Pair(CoatingType.POISON, coating_poison),
                Pair(CoatingType.PARALYSIS, coating_paralysis), Pair(CoatingType.SLEEP, coating_sleep), Pair(CoatingType.BLAST, coating_blast))

        return buffer.filter { (_, value) -> value != null && value > 0 }.map { x -> x.key }.toList().iterator()
    }
}

/**
 * The result of fully loading a weapon.
 * Loads the data you'd find in an weapon screen ingame.
 */
class WeaponFull(
        val weapon: Weapon,
        val ammo: WeaponAmmoData?,
        val melodies: List<WeaponMelody>,
        val recipe: Map<String?, List<ItemQuantity>>,
        val skills: List<SkillLevel>,
        val setBonus: List<ArmorSetBonus>
) : MHModel {
    override val entityId get() = weapon.id
    override val entityType get() = DataType.WEAPON

    constructor(weapon: Weapon, skills: List<SkillLevel>, setBonus: List<ArmorSetBonus>)
            : this(weapon, null, emptyList(), emptyMap(), skills, setBonus)
}

data class WeaponAmmoData(
        val ammo_id: Int,
        val deviation: String?,
        val special_ammo: SpecialAmmoType?,
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
) : Iterable<WeaponAmmo> {
    override fun iterator(): Iterator<WeaponAmmo> {
        val buffer = listOf(
                WeaponAmmo(AmmoType.NORMAL_AMMO1, normal1_clip, normal1_rapid, normal1_reload, normal1_recoil),
                WeaponAmmo(AmmoType.NORMAL_AMMO2, normal2_clip, normal2_rapid, normal2_reload, normal2_recoil),
                WeaponAmmo(AmmoType.NORMAL_AMMO3, normal3_clip, normal3_rapid, normal3_reload, normal3_recoil),
                WeaponAmmo(AmmoType.PIERCE_AMMO1, pierce1_clip, pierce1_rapid, pierce1_reload, pierce1_recoil),
                WeaponAmmo(AmmoType.PIERCE_AMMO2, pierce2_clip, pierce2_rapid, pierce2_reload, pierce2_recoil),
                WeaponAmmo(AmmoType.PIERCE_AMMO3, pierce3_clip, pierce3_rapid, pierce3_reload, pierce3_recoil),
                WeaponAmmo(AmmoType.SPREAD_AMMO1, spread1_clip, spread1_rapid, spread1_reload, spread1_recoil),
                WeaponAmmo(AmmoType.SPREAD_AMMO2, spread2_clip, spread2_rapid, spread2_reload, spread2_recoil),
                WeaponAmmo(AmmoType.SPREAD_AMMO3, spread3_clip, spread3_rapid, spread3_reload, spread3_recoil),
                WeaponAmmo(AmmoType.STICKY_AMMO1, sticky1_clip, sticky1_rapid, sticky1_reload, sticky1_recoil),
                WeaponAmmo(AmmoType.STICKY_AMMO2, sticky2_clip, sticky2_rapid, sticky2_reload, sticky2_recoil),
                WeaponAmmo(AmmoType.STICKY_AMMO3, sticky3_clip, sticky3_rapid, sticky3_reload, sticky3_recoil),
                WeaponAmmo(AmmoType.CLUSTER_AMMO1, cluster1_clip, cluster1_rapid, cluster1_reload, cluster1_recoil),
                WeaponAmmo(AmmoType.CLUSTER_AMMO2, cluster2_clip, cluster2_rapid, cluster2_reload, cluster2_recoil),
                WeaponAmmo(AmmoType.CLUSTER_AMMO3, cluster3_clip, cluster3_rapid, cluster3_reload, cluster3_recoil),
                WeaponAmmo(AmmoType.RECOVER_AMMO1, recover1_clip, recover1_rapid, recover1_reload, recover1_recoil),
                WeaponAmmo(AmmoType.RECOVER_AMMO2, recover2_clip, recover2_rapid, recover2_reload, recover2_recoil),
                WeaponAmmo(AmmoType.POISON_AMMO1, poison1_clip, poison1_rapid, poison1_reload, poison1_recoil),
                WeaponAmmo(AmmoType.POISON_AMMO2, poison2_clip, poison2_rapid, poison2_reload, poison2_recoil),
                WeaponAmmo(AmmoType.PARALYSIS_AMMO1, paralysis1_clip, paralysis1_rapid, paralysis1_reload, paralysis1_recoil),
                WeaponAmmo(AmmoType.PARALYSIS_AMMO2, paralysis2_clip, paralysis2_rapid, paralysis2_reload, paralysis2_recoil),
                WeaponAmmo(AmmoType.SLEEP_AMMO1, sleep1_clip, sleep1_rapid, sleep1_reload, sleep1_recoil),
                WeaponAmmo(AmmoType.SLEEP_AMMO2, sleep2_clip, sleep2_rapid, sleep2_reload, sleep2_recoil),
                WeaponAmmo(AmmoType.EXHAUST_AMMO1, exhaust1_clip, exhaust1_rapid, exhaust1_reload, exhaust1_recoil),
                WeaponAmmo(AmmoType.EXHAUST_AMMO2, exhaust2_clip, exhaust2_rapid, exhaust2_reload, exhaust2_recoil),
                WeaponAmmo(AmmoType.FLAMING_AMMO, flaming_clip, flaming_rapid, flaming_reload, flaming_recoil),
                WeaponAmmo(AmmoType.WATER_AMMO, water_clip, water_rapid, water_reload, water_recoil),
                WeaponAmmo(AmmoType.FREEZE_AMMO, freeze_clip, freeze_rapid, freeze_reload, freeze_recoil),
                WeaponAmmo(AmmoType.THUNDER_AMMO, thunder_clip, thunder_rapid, thunder_reload, thunder_recoil),
                WeaponAmmo(AmmoType.DRAGON_AMMO, dragon_clip, dragon_rapid, dragon_reload, dragon_recoil),
                WeaponAmmo(AmmoType.SLICING_AMMO, slicing_clip, slicing_rapid, slicing_reload, slicing_recoil),
                WeaponAmmo(AmmoType.WYVERN_AMMO, wyvern_clip, false, wyvern_reload, 0),
                WeaponAmmo(AmmoType.DEMON_AMMO, demon_clip, false, demon_reload, demon_recoil),
                WeaponAmmo(AmmoType.ARMOR_AMMO, armor_clip, false, armor_reload, armor_recoil),
                WeaponAmmo(AmmoType.TRANQ_AMMO, tranq_clip, false, tranq_reload, tranq_recoil))

        return buffer.filter { x -> x.capacity > 0 }.iterator()
    }
}

class WeaponAmmo(
        var type: AmmoType,
        var capacity: Int,
        var isRapid: Boolean,
        var reload: ReloadType,
        var recoil: Int
)

class WeaponMelody(
        val id: Int,
        val notes: String,
        val base_duration: Int?,
        val base_extension:Int?,
        val m1_duration:Int?,
        val m1_extension:Int?,
        val m2_duration:Int?,
        val m2_extension:Int?,
        val effect1: String,
        val effect2: String
)