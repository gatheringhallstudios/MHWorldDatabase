package com.gatheringhallstudios.mhworlddatabase.common

class EquipmentFilter<T>(
        var equipmentList: List<T>?
) {
    private val filterList = mutableListOf<Filter<T>>()

    /**
     * Clears registered filters.
     */
    fun clearFilters() {
        filterList.clear()
    }

    /**
     * Adds a filter to the filter list.
     * The filter is applied during renderResults()
     */
    fun addFilter(filter: Filter<T>) {
        filterList.add(filter)
    }

    fun renderResults(): List<T> {
        if (equipmentList.isNullOrEmpty()) return emptyList()
        return equipmentList!!.filter { listItem -> filterList.all{ it.runFilter(listItem)} }
    }
}