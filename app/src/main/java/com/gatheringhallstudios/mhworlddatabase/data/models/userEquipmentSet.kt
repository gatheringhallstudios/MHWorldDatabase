package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
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
        fun createEmptySet(): UserEquipmentSet {
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
    var skills = listOf<SkillLevel>()
    var setBonuses = mutableMapOf<String, List<ArmorSetBonus>>()

    fun getWeapon(): UserWeapon? {
        return equipment.find { it.type() == DataType.WEAPON } as? UserWeapon
    }

    fun getHeadArmor(): UserArmorPiece? {
        return equipment.filter { it.type() == DataType.ARMOR }.find {
            (it as? UserArmorPiece)?.armor?.armor?.armor_type == ArmorType.HEAD
        } as? UserArmorPiece
    }

    fun getArmArmor(): UserArmorPiece? {
        return equipment.filter { it.type() == DataType.ARMOR }.find {
            (it as UserArmorPiece).armor.armor.armor_type == ArmorType.ARMS
        } as? UserArmorPiece
    }

    fun getChestArmor(): UserArmorPiece? {
        return equipment.filter { it.type() == DataType.ARMOR }.find {
            (it as UserArmorPiece).armor.armor.armor_type == ArmorType.CHEST
        } as? UserArmorPiece
    }

    fun getWaistArmor(): UserArmorPiece? {
        return equipment.filter { it.type() == DataType.ARMOR }.find {
            (it as UserArmorPiece).armor.armor.armor_type == ArmorType.WAIST
        } as? UserArmorPiece
    }


    fun getLegArmor(): UserArmorPiece? {
        return equipment.filter { it.type() == DataType.ARMOR }.find {
            (it as UserArmorPiece).armor.armor.armor_type == ArmorType.LEGS
        } as? UserArmorPiece
    }

    fun getCharm(): UserCharm? {
        return equipment.find { it.type() == DataType.CHARM } as? UserCharm
    }
}

interface UserEquipment : Serializable {
    fun setId(): Int
    fun entityId(): Int
    fun type(): DataType
}

class UserArmorPiece(
        val equipmentSetId: Int,
        val armor: ArmorFull,
        val decorations: List<UserDecoration>
) : UserEquipment {
    override fun setId(): Int {
        return equipmentSetId
    }

    override fun entityId(): Int {
        return armor.armor.id
    }

    override fun type(): DataType {
        return DataType.ARMOR
    }
}

class UserWeapon(
        val equipmentSetId: Int,
        val weapon: WeaponFull,
        val decorations: List<UserDecoration>
) : UserEquipment {
    override fun setId(): Int {
        return equipmentSetId
    }

    override fun entityId(): Int {
        return weapon.weapon.id
    }

    override fun type(): DataType {
        return DataType.WEAPON
    }
}

class UserCharm(
        val equipmentSetId: Int,
        val charm: CharmFull
) : UserEquipment {
    override fun setId(): Int {
        return equipmentSetId
    }

    override fun entityId(): Int {
        return charm.charm.id
    }

    override fun type(): DataType {
        return DataType.CHARM
    }
}

class UserDecoration(
        val equipmentSetId: Int,
        val decoration: Decoration,
        val slotNumber: Int
) : UserEquipment {
    override fun setId(): Int {
        return equipmentSetId
    }

    override fun entityId(): Int {
        return decoration.id
    }

    override fun type(): DataType {
        return DataType.DECORATION
    }
}