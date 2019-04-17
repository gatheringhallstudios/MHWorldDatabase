package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

class UserEquipmentSetIds(
        val id: Int,
        val name: String,
        val equipmentIds: MutableList<UserEquipmentIds>
)

class UserEquipmentIds(
        val dataId: Int,
        val equipmentSetId: Int,
        val dataType: DataType,
        val decorationIds: MutableList<UserDecorationIds>
)

class UserDecorationIds(
        val decorationId: Int
)

class UserEquipmentSet(
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
    var skills = mutableMapOf<Int, SkillLevel>()
}

interface UserEquipment {
    fun getType(): DataType
}

class UserArmorPiece (
        val armor : ArmorFull,
        val decorations: List<Decoration>
) : UserEquipment {
    override fun getType(): DataType {
        return DataType.ARMOR
    }
}

class UserWeapon (
        val weapon : WeaponFull,
        val decorations : List<Decoration>
) : UserEquipment {
    override fun getType(): DataType {
        return DataType.WEAPON
    }
}

class UserCharm (
        val charm : CharmFull
) : UserEquipment {
    override fun getType(): DataType {
        return DataType.CHARM
    }
}
