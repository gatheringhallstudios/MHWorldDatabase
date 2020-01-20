package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import com.gatheringhallstudios.mhworlddatabase.util.tree.Filter
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.*

class WeaponElementFilter(private val elements: Set<ElementStatus>): Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.element1 in elements || obj.element2 in elements)
    }
}

class WeaponPhialFilter(private val phialTypes: Set<PhialType>): Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.phial in phialTypes)
    }
}

class WeaponKinsectFilter(private val kinsectBonusTypes: Set<KinsectBonus>): Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.kinsect_bonus in kinsectBonusTypes)
    }
}

class WeaponShellingFilter(private val shellingTypes: Set<ShellingType>): Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.shelling in shellingTypes)
    }
}

class WeaponShellingLevelFilter(private val levels: Set<Int>): Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return (obj.shelling_level in levels)
    }
}

/**
 * Filter that resolves if every coating type given is a valid one for the weapon
 */
class WeaponCoatingFilter(private val coatings: Set<CoatingType>): Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return coatings.all { obj.weaponCoatings?.contains(it) ?: false }
    }
}

class WeaponSpecialAmmoFilter(private val specialAmmo: SpecialAmmoType?): Filter<Weapon> {
    override fun runFilter(obj: Weapon): Boolean {
        return obj.special_ammo == specialAmmo
    }
}