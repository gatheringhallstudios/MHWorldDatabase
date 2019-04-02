package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

class UserEquipmentSet (
        val id: Int,
        val equipment: MutableList<UserEquipment>
)

class UserEquipment (
        val dataId: Int,
        val equipmentSetId: Int,
        val dataType: DataType,
        val decorations: MutableList<UserDecoration>
)

class UserDecoration (
        val decorationId: Int
)