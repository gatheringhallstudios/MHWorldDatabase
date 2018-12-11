package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.entities.Language

@Dao
abstract class MetaDao {
    /**
     * Queries for all languages supported by the database
     * @return
     */
    @Query("SELECT id, name from language")
    abstract fun queryLanguages(): List<Language>
}