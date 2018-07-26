package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem
import com.gatheringhallstudios.mhworlddatabase.data.models.Location

/**
 * A class used to retrieve data centered around Habitats, including item availability data and monsters available.
 * Retrieve a copy from MHWDatabase.locationDao().
 */
@Dao
abstract class LocationDao {
    @Query("""
        SELECT id, name
        FROM location_text t
        WHERE t.lang_id = :langId
        ORDER BY order_id ASC
        """)
    abstract fun loadLocations(langId: String) : LiveData<List<Location>>

    @Query("""
        SELECT id, name
        FROM location_text t
        WHERE t.lang_id = :langId
          AND t.id = :locationId""")
    abstract fun loadLocation(langId : String, locationId: Int) : LiveData<Location>

    @Query("""
        SELECT i.id item_id, it.name item_name, i.icon_name item_icon_name, i.icon_color item_icon_color, i.category item_category,
            li.rank, li.area, li.stack, li.percentage, li.nodes
        FROM location_item li
            JOIN item i
                ON i.id = li.item_id
            JOIN item_text it
                ON it.id = i.id
        WHERE li.location_id = :locationId
          AND it.lang_id = :langId
        """)
    abstract fun loadLocationItems(langId: String, locationId : Int) : LiveData<List<LocationItem>>
}