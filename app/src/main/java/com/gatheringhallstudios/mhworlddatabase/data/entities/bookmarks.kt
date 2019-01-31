package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import java.util.*

@Entity(tableName = "bookmarks",
        primaryKeys = ["dataId", "dataType"])
data class BookmarkEntity(
        val dataId: Int,
        val dataType: DataType,
        val dateAdded: Date
)