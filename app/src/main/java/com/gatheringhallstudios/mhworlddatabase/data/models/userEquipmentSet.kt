package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

open class UserEquipmentSet (
        val id: Int,
        val name: String,
        val equipment: MutableList<UserEquipment>
) {
    var defense_base: Int = 0
    var defense_max: Int = 0
    var defense_augment_max: Int = 0
    var fireDefense: Int = 0
    var waterDefense: Int = 0
    var thunderDefense: Int = 0
    var iceDefense: Int = 0
    var dragonDefense: Int = 0
    var skills = mutableListOf<SkillLevel>()
}

class UserEquipment (
        val dataId: Int,
        val equipmentSetId: Int,
        val dataType: DataType,
        val decorations: MutableList<UserDecoration>
) {

}

class UserDecoration (
        val decorationId: Int
)