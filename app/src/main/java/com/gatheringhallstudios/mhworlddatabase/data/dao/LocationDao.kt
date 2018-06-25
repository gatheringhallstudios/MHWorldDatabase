package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationItemView
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHabitatView

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
        ORDER BY id ASC
        """)
    abstract fun loadLocations(langId: String) : LiveData<List<LocationView>>

    @Query("""
        SELECT id, name
        FROM location_text t
        WHERE t.lang_id = :langId
          AND t.id = :locationId""")
    abstract fun loadLocation(langId : String, locationId: Int) : LiveData<LocationView>

    @Query("""
        SELECT li.*, it.name item_name
        FROM location_item li
            JOIN item_text it
                ON it.id = li.item_id
        WHERE li.location_id = :locationId
          AND it.lang_id = :langId
        """)
    abstract fun loadLocationItems(langId: String, locationId : Int) : LiveData<List<LocationItemView>>
}