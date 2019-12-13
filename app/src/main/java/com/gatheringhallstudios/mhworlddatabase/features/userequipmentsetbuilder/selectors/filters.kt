package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import com.gatheringhallstudios.mhworlddatabase.common.Filter
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorFull
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmFull
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponFull
import com.gatheringhallstudios.mhworlddatabase.data.types.ElementStatus
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

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

class CharmNameFilter(private val name: String) : Filter<CharmFull> {
    override fun runFilter(obj: CharmFull): Boolean {
        return obj.charm.name?.contains(name, ignoreCase = true) ?: false
    }
}

class WeaponNameFilter(private val name: String) : Filter<WeaponFull> {
    override fun runFilter(obj: WeaponFull): Boolean {
        return obj.weapon.name.contains(name, ignoreCase = true)
    }
}

class WeaponSlotFilter(private val slotLevels: Set<Int>) : Filter<WeaponFull> {
    override fun runFilter(obj: WeaponFull): Boolean {
        return obj.weapon.slots.active.any { slotLevels.contains(it) }
    }
}

class WeaponTypeFilter(private val weaponTypes: Set<WeaponType>) : Filter<WeaponFull> {
    override fun runFilter(obj: WeaponFull): Boolean {
        return weaponTypes.contains(obj.weapon.weapon_type)
    }
}

class WeaponElementFilter(private val elements: Set<ElementStatus>) : Filter<WeaponFull> {
    override fun runFilter(obj: WeaponFull): Boolean {
        return elements.contains(obj.weapon.element1) || elements.contains(obj.weapon.element2)
    }
}