package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import java.util.*

@Entity(tableName = "favorites",
        primaryKeys = ["dataId", "dataType"])
data class FavoriteEntity(
        val dataId: Int,
        val dataType: DataType,
        val dateAdded: Date
)