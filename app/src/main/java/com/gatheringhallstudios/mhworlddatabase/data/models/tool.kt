package com.gatheringhallstudios.mhworlddatabase.data.models

import androidx.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.ToolType

class Tool(
        id: Int,
        order_id: Int,
        icon_color: String?,
        name: String?,
        name_base: String?,
        tool_type: ToolType,

        val description: String?,
        val duration: Int,
        val duration_upgraded: Int?,
        val recharge: Int

        ) : ToolBase(id, order_id, icon_color, name, name_base, tool_type), MHModel {
    /**
     * A list of slot level values (0-3) that can be iterated on.
     */
    @Embedded
    lateinit var slots: EquipmentSlots

    override val entityId get() = id
    override val entityType get() = DataType.TOOL
}

open class ToolBase(val id: Int,
                    val order_id: Int,
                    val icon_color: String?,
                    val name: String?,
                    val name_base: String?,
                    val tool_type: ToolType
)