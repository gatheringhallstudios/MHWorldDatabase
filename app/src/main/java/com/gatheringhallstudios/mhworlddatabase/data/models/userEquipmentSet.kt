package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import java.io.Serializable

class UserEquipmentSetIds(
        val id: Int,
        val name: String,
        val equipmentIds: MutableList<UserEquipmentIds>
) : Serializable

class UserEquipmentIds(
        val dataId: Int,
        val equipmentSetId: Int,
        val dataType: DataType,
        val decorationIds: MutableList<UserDecorationIds>
)

class UserDecorationIds(
        val decorationId: Int,
        val slotNumber: Int
)

class UserEquipmentSet(
        val id: Int,
        val name: String,
        val equipment: MutableList<UserEquipment>
) : Serializable {
    companion object {
        fun createEmptySet() : UserEquipmentSet {
            return UserEquipmentSet(0, "", mutableListOf())
        }
    }
    var defense_base: Int = 0
    var defense_max: Int = 0
    var defense_augment_max: Int = 0
    var fireDefense: Int = 0
    var waterDefense: Int = 0
    var thunderDefense: Int = 0
    var iceDefense: Int = 0
    var dragonDefense: Int = 0
    var skills = mutableMapOf<Int, SkillLevel>()
    var setBonuses = mutableMapOf<String, List<ArmorSetBonus>>()
}

interface UserEquipment : Serializable {
    fun entityId(): Int
    fun type(): DataType
}

class UserArmorPiece(
        val armor: ArmorFull,
        val decorations: List<UserDecoration>
) : UserEquipment {
    override fun entityId(): Int {
        return armor.armor.id
    }

    override fun type(): DataType {
        return DataType.ARMOR
    }
}

class UserWeapon(
        val weapon: WeaponFull,
        val decorations: List<UserDecoration>
) : UserEquipment {
    override fun entityId(): Int {
        return weapon.weapon.id
    }

    override fun type(): DataType {
        return DataType.WEAPON
    }
}

class UserCharm(
        val charm: CharmFull
) : UserEquipment {
    override fun entityId(): Int {
        return charm.charm.id
    }

    override fun type(): DataType {
        return DataType.CHARM
    }
}

class UserDecoration(
        val decoration: Decoration,
        val slotNumber: Int

) : UserEquipment {
    override fun entityId(): Int {
        return decoration.id
    }

    override fun type(): DataType {
        return DataType.DECORATION
    }
}