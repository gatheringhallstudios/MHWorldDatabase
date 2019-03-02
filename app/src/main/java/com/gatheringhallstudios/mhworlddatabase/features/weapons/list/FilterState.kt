package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import com.gatheringhallstudios.mhworlddatabase.data.types.ElementStatus
import com.gatheringhallstudios.mhworlddatabase.data.types.PhialType
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
        var phials: Set<PhialType>
): Serializable {
    companion object {
        @JvmStatic
        val default = FilterState(
                isFinalOnly = false,
                sortBy = FilterSortCondition.NONE,
                elements = emptySet(),
                phials = emptySet()
        )
    }
}