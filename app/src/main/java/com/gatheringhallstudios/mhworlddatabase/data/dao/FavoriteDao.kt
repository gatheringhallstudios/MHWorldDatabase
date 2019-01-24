package com.gatheringhallstudios.mhworlddatabase.data.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.gatheringhallstudios.mhworlddatabase.data.models.Favorite
import com.gatheringhallstudios.mhworlddatabase.data.entities.FavoriteEntity
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

@Dao
abstract class FavoriteDao {
    @Query("""
        SELECT f.dataId, f.dataType, f.dateAdded
        FROM favorites f """)
    abstract fun loadFavorites():MutableList<Favorite>

//    @Query("""
//        SELECT f.dataId, f.dataType, f.dateAdded
//        FROM favorites f
//        WHERE f.dataType = :dataType AND f.dataId = :dataId
//        GROUP BY dataType""")
//    abstract fun loadFavorite(dataType:String, dataId: Int): Favorite?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(entities: FavoriteEntity)

    @Delete
    abstract fun delete(entities: FavoriteEntity)

    fun getFavorites
}