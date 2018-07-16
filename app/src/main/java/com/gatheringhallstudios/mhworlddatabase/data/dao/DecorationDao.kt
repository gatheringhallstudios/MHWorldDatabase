package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.data.views.*

@Dao
abstract class DecorationDao {
    @Query("""
        SELECT d.*, dt.name
        FROM decoration d
            JOIN decoration_text dt
                ON dt.id = d.id
                AND dt.lang_id = :langId
        ORDER BY dt.name""")
    abstract fun loadDecorations(langId: String): LiveData<List<DecorationView>>

    @Query("""
        SELECT d.*, dt.name
        FROM decoration d
            JOIN decoration_text dt
                ON dt.id = d.id
                AND dt.lang_id = :langId
        WHERE d.id = :decorationId""")
    abstract fun loadDecoration(langId: String, decorationId: Int): LiveData<DecorationView>
}
