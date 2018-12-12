package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationCamp

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
    abstract fun loadLocations(langId: String): LiveData<List<Location>>

    @Query("""
        SELECT id, name
        FROM location_text t
        WHERE t.lang_id = :langId
          AND t.id = :locationId""")
    abstract fun loadLocation(langId: String, locationId: Int): LiveData<Location>

    @Query("""
        SELECT lct.name, lct.area
        FROM location_camp_text lct
        WHERE lct.location_id = :locationId
          AND lct.lang_id = :langId
        ORDER BY lct.id ASC
    """)
    abstract fun loadLocationCamps(langId: String, locationId: Int): LiveData<List<LocationCamp>>

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
    abstract fun loadLocationItems(langId: String, locationId: Int): LiveData<List<LocationItem>>
}