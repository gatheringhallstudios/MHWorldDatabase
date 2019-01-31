package com.gatheringhallstudios.mhworlddatabase.data.dao


import androidx.room.*
import com.gatheringhallstudios.mhworlddatabase.data.models.Bookmark
import com.gatheringhallstudios.mhworlddatabase.data.entities.BookmarkEntity


@Dao
abstract class BookmarksDao {
    @Query("""
        SELECT b.dataId, b.dataType, b.dateAdded
        FROM bookmarks b """)
    abstract fun loadBookmarks():MutableList<Bookmark>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(entities: BookmarkEntity)

    @Delete
    abstract fun delete(entities: BookmarkEntity)
}