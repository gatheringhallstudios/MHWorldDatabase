package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import com.gatheringhallstudios.mhworlddatabase.data.types.ElementStatus
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectBonus
import com.gatheringhallstudios.mhworlddatabase.data.types.PhialType
import com.gatheringhallstudios.mhworlddatabase.data.types.ShellingType
import java.io.Serializable

enum class FilterSortCondition {
    NONE,
    ATTACK,
    AFFINITY
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
        var shellingLevels: Set<Int>
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
                shellingLevels = emptySet()
        )
    }
}