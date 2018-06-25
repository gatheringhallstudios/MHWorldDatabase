package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemLocationView
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView

@Dao
abstract class ItemDao {
    @Query("""
        SELECT i.*, it.name, it.description, i.category
        FROM item i
            JOIN item_text it
                ON it.id = i.id
                AND it.lang_id = :langId
        WHERE (:category IS NULL AND i.category != 'hidden') OR i.category = :category
        ORDER BY i.id""")
    abstract fun loadItems(langId: String, category: ItemCategory? = null): LiveData<List<ItemView>>

    @Query("""
        SELECT i.*, it.name, it.description
        FROM item i
        JOIN item_text it
            ON it.id = i.id
            AND it.lang_id = :langId
        WHERE i.id = :itemId """)
    abstract fun loadItem(langId: String, itemId: Int): LiveData<ItemView>

    @Query("""
        SELECT li.*, lt.name location_name
        FROM location_item li
            JOIN location_text lt
                ON lt.id = li.location_id
        WHERE li.item_id = :itemId
          AND lt.lang_id = :langId
        """)
    abstract fun loadItemLocations(langId: String, itemId : Int) : LiveData<List<ItemLocationView>>

//    @Query("""
//        SELECT *
//        FROM item_combination
//        WHERE result_id = :itemId
//            OR first_id = :itemId
//            OR second_id = :itemId""")
//    abstract fun loadRawItemCombos(langId: String, itemId: Int): LiveData<ItemCombinationEntity>

//    fun loadFullItemCombos(langId: String, itemId: Int): LiveData<ItemCombinationView> {
//        val itemCombos = loadRawItemCombos(langId, itemId)
//
//        return Transformations.map(itemCombos) { data ->
//            val result = loadItem(langId, data.result_id).value
//            val first = loadItem(langId, data.first_id).value
//            val second = if (data.second_id != null) {
//                loadItem(langId, data.second_id).value
//            } else null
//
//            ItemCombinationView(
//                    result = result,
//                    first = first,
//                    second = second
//            )
//        }
//    }
}
