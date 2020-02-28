package com.gatheringhallstudios.mhworlddatabase.features.kinsects.list

import com.gatheringhallstudios.mhworlddatabase.data.types.*
import java.io.Serializable

enum class FilterSortCondition {
    NONE,
    POWER,
    SPEED,
    HEAL
}

data class FilterState(
        var isFinalOnly: Boolean,
        var sortBy: FilterSortCondition,
        var attackTypes: Set<KinsectAttackType>,
        var dustEffects: Set<KinsectDustEffect>
): Serializable {
    companion object {
        @JvmStatic
        val default = FilterState(
                isFinalOnly = false,
                sortBy = FilterSortCondition.NONE,
                attackTypes = emptySet(),
                dustEffects = emptySet()
        )
    }
}