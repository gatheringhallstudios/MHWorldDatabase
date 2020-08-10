package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gatheringhallstudios.mhworlddatabase.data.types.ToolType

@Entity(tableName = "tool")
data class ToolEntity(
        @PrimaryKey val id: Int,
        val order_id: Int,
        val icon_color: String?,

        val tool_type: ToolType,

        val duration: Int,
        val duration_upgraded: Int,
        val recharge: Int,


        val slot_1: Int,
        val slot_2: Int,
        val slot_3: Int
)

@Entity(tableName = "tool_text",
        primaryKeys = ["id", "lang_id"])
data class ToolText(
        val id: Int,
        val lang_id: String,

        val name: String?,
        val name_base: String?,
        val description: String?
)