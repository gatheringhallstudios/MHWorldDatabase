package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemCombination
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemLocation
import com.gatheringhallstudios.mhworlddatabase.data.models.Item

// columns for a combination result set
private const val combinationColumns = """
     c.id,
        r.id result_id, rt.name result_name, r.icon_name result_icon_name, r.icon_color result_icon_color,
        f.id first_id, ft.name first_name, f.icon_name first_icon_name, f.icon_color first_icon_color,
        s.id second_id, st.name second_name, s.icon_name second_icon_name, s.icon_color second_icon_color,
        c.quantity quantity
"""

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
    abstract fun loadItems(langId: String, category: ItemCategory? = null): LiveData<List<Item>>

    @Query("""
        SELECT i.*, it.name, it.description
        FROM item i
        JOIN item_text it
            ON it.id = i.id
            AND it.lang_id = :langId
        WHERE i.id = :itemId """)
    abstract fun loadItem(langId: String, itemId: Int): LiveData<Item>

    @Query("""
        SELECT li.*, lt.name location_name
        FROM location_item li
            JOIN location_text lt
                ON lt.id = li.location_id
        WHERE li.item_id = :itemId
          AND lt.lang_id = :langId
        """)
    abstract fun loadItemLocations(langId: String, itemId : Int) : LiveData<List<ItemLocation>>

    @Query("""
        SELECT $combinationColumns
        FROM item_combination c
            JOIN item r
                ON r.id = c.result_id
            JOIN item_text rt
                ON rt.id = r.id
                AND rt.lang_id = :langId
            JOIN item f
                ON f.id = c.first_id
            JOIN item_text ft
                ON ft.id = f.id
                AND ft.lang_id = :langId
            LEFT JOIN item s
                ON s.id = c.second_id
            LEFT JOIN item_text st
                ON st.id = s.id
                AND st.lang_id = :langId
                """)
    abstract fun loadItemCombinations(langId: String) : LiveData<List<ItemCombination>>

    @Query("""
        SELECT $combinationColumns
        FROM item_combination c
            JOIN item r
                ON r.id = c.result_id
            JOIN item_text rt
                ON rt.id = r.id
                AND rt.lang_id = :langId
            JOIN item f
                ON f.id = c.first_id
            JOIN item_text ft
                ON ft.id = f.id
                AND ft.lang_id = :langId
            LEFT JOIN item s
                ON s.id = c.second_id
            LEFT JOIN item_text st
                ON st.id = s.id
                AND st.lang_id = :langId
        WHERE c.result_id = :itemId
          OR c.first_id = :itemId
          OR c.second_id = :itemId
                """)
    abstract fun loadItemCombinationsFor(langId: String, itemId: Int) : LiveData<List<ItemCombination>>
}
