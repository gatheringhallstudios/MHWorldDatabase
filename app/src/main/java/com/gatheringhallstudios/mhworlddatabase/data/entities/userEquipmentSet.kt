package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

@Entity(tableName = "user_equipment_sets")
data class UserEquipmentSetEntity(
        val dataId: Int,
        val dataType: DataType
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity(tableName = "user_equipment_set_decorations")
data class UserEquipmentDecorationEntity(
        val equipmentSetId: Int,
        val dataId: Int,
        val dataType: DataType,
        val decorationId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
