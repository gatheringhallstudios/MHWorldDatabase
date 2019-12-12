package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import com.gatheringhallstudios.mhworlddatabase.common.Filter
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorFull
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.*

class ArmorNameFilter(private val name: String) : Filter<ArmorFull> {
    override fun runFilter(obj: ArmorFull): Boolean {
        return obj.armor.name?.contains(name, ignoreCase = true) ?: false
    }
}

class ArmorRankFilter(private val ranks: Set<Rank>) : Filter<ArmorFull> {
    override fun runFilter(obj: ArmorFull): Boolean {
        return ranks.contains(obj.armor.rank)
    }
}

class ArmorElementalDefenseFilter(private val elementDefs: Set<ElementStatus>) : Filter<ArmorFull> {
    override fun runFilter(obj: ArmorFull): Boolean {
        elementDefs.forEach {
            when (it) {
                ElementStatus.FIRE -> {
                    if (obj.armor.fire > 0) return true
                }
                ElementStatus.WATER -> {
                    if (obj.armor.water > 0) return true
                }
                ElementStatus.THUNDER -> {
                    if (obj.armor.thunder > 0) return true
                }
                ElementStatus.ICE -> {
                    if (obj.armor.ice > 0) return true
                }
                ElementStatus.DRAGON -> {
                    if (obj.armor.dragon > 0) return true
                }
                else -> {
                }
            }
        }

        return false
    }
}

class DecorationNameFilter(private val name: String) : Filter<Decoration> {
    override fun runFilter(obj: Decoration): Boolean {
        return obj.name?.contains(name, ignoreCase = true) ?: false
    }
}

class DecorationSlotLevelFilter(private val slotLevels: Set<Int>) : Filter<Decoration> {
    override fun runFilter(obj: Decoration): Boolean {
        return slotLevels.contains(obj.slot)
    }
}

class WeaponElementFilter(private val elements: Set<ElementStatus>) : Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.element1 in elements || obj.element2 in elements)
    }
}

class WeaponPhialFilter(private val phialTypes: Set<PhialType>) : Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.phial in phialTypes)
    }
}

class WeaponKinsectFilter(private val kinsectBonusTypes: Set<KinsectBonus>) : Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.kinsect_bonus in kinsectBonusTypes)
    }
}

class WeaponShellingFilter(private val shellingTypes: Set<ShellingType>) : Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.shelling in shellingTypes)
    }
}

class WeaponShellingLevelFilter(private val levels: Set<Int>) : Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.shelling_level in levels)
    }
}

/**
 * Filter that resolves if every coating type given is a valid one for the weapon
 */
class WeaponCoatingFilter(private val coatings: Set<CoatingType>) : Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return coatings.all { obj.weaponCoatings?.contains(it) ?: false }
    }
}

class WeaponSpecialAmmoFilter(private val specialAmmo: SpecialAmmoType?) : Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return obj.special_ammo == specialAmmo
    }
}