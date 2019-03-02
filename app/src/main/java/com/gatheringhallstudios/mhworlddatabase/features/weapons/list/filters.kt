package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import com.gatheringhallstudios.mhworlddatabase.common.Filter
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.ElementStatus
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectBonus
import com.gatheringhallstudios.mhworlddatabase.data.types.PhialType
import com.gatheringhallstudios.mhworlddatabase.data.types.ShellingType

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