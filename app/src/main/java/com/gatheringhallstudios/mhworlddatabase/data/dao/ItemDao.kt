package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory

import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView

@Dao
abstract class ItemDao {
    @Query("""
        SELECT i.*, it.name, it.description, i.category
        FROM item i
            JOIN item_text it
                ON it.id = i.id
                AND it.lang_id = :langId
        WHERE :category IS NULL OR i.category = :category
        ORDER BY i.id""")
    abstract fun getItems(langId: String, category: ItemCategory? = null): LiveData<List<ItemView>>

    @Query("""
        SELECT i.*, it.name, it.description
        FROM item i
        JOIN item_text it
            ON it.id = i.id
            AND it.lang_id = :langId
        WHERE i.id = :itemId """)
    abstract fun getItem(langId: String, itemId: Int): LiveData<ItemView>
}
