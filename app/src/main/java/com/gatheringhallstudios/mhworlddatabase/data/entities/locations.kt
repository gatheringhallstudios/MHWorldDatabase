package com.gatheringhallstudios.mhworlddatabase.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank


@Entity(tableName = "location_text", primaryKeys = ["id", "lang_id"])
data class LocationText(
        val id: Int,
        val order_id: Int,
        val lang_id: String,
        val name: String?
)

@Entity(tableName = "location_item")
data class LocationItemEntity(
        @PrimaryKey val id: Int,
        val location_id: Int,
        val rank: Rank?,
        val area: Int,
        val item_id: Int,
        val stack: Int,
        val percentage: Int,
        val nodes: Int
)

@Entity(tableName = "location_camp_text")
data class LocationCampText(
        @PrimaryKey val id: Int,
        val location_id: Int,
        val lang_id: String,
        val name: String?,
        val area: Int
)