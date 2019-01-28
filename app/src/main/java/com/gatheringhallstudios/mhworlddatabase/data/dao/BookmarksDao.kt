package com.gatheringhallstudios.mhworlddatabase.data.dao


import androidx.room.*
import com.gatheringhallstudios.mhworlddatabase.data.models.Bookmark
import com.gatheringhallstudios.mhworlddatabase.data.entities.BookmarkEntity


@Dao
abstract class BookmarksDao {
    @Query("""
        SELECT f.dataId, f.dataType, f.dateAdded
        FROM favorites f """)
    abstract fun loadBookmarks():MutableList<Bookmark>

//    @Query("""
//        SELECT f.dataId, f.dataType, f.dateAdded
//        FROM favorites f
//        WHERE f.dataType = :dataType AND f.dataId = :dataId
//        GROUP BY dataType""")
//    abstract fun loadFavorite(dataType:String, dataId: Int): Bookmark?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(entities: BookmarkEntity)

    @Delete
    abstract fun delete(entities: BookmarkEntity)
}