package com.gatheringhallstudios.mhworlddatabase.common

class EquipmentFilter<T>(
        var list: List<T>
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
        if (list.isEmpty()) return emptyList()

        return list.filter { listItem -> filterList.all{ it.runFilter(listItem)} }
    }
}