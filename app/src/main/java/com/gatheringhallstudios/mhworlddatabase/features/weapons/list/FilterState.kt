package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import com.gatheringhallstudios.mhworlddatabase.data.types.*
import java.io.Serializable

enum class FilterSortCondition {
    NONE,
    ATTACK,
    AFFINITY,
    ELEMENT_STATUS
}

/**
 * The current state of a filter request.
 * Only contains the configuration settings and does no logic.
 */
data class FilterState(
        var isFinalOnly: Boolean,
        var sortBy: FilterSortCondition,
        var elements: Set<ElementStatus>,
        var phials: Set<PhialType>,
        var kinsectBonuses: Set<KinsectBonus>,
        var shellingTypes: Set<ShellingType>,
        var shellingLevels: Set<Int>,
        var coatingTypes: Set<CoatingType>,
        var specialAmmo: SpecialAmmoType?
): Serializable {
    companion object {
        @JvmStatic
        val default = FilterState(
                isFinalOnly = false,
                sortBy = FilterSortCondition.NONE,
                elements = emptySet(),
                phials = emptySet(),
                kinsectBonuses = emptySet(),
                shellingTypes = emptySet(),
                shellingLevels = emptySet(),
                coatingTypes = emptySet(),
                specialAmmo = null
        )
    }
}