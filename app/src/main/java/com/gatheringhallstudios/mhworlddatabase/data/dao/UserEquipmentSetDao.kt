package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.room.*
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentDecorationEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentSetEntity
import com.gatheringhallstudios.mhworlddatabase.data.models.UserDecorationIds
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentIds
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSetIds
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

@Dao
abstract class UserEquipmentSetDao {
    @Query("""
        SELECT u.id, u.name
        FROM user_equipment_sets u """)
    abstract fun loadUserEquipmentSetInfo(): List<UserEquipmentSetEntity>

    @Query("""
        SELECT u.id, u.name
        FROM user_equipment_sets u
        WHERE u.id = :setId""")
    abstract fun loadUserEquipmentSetInfo(setId: Int): UserEquipmentSetEntity

    @Query("""
        SELECT u.id, u.equipmentSetId, u.dataId, u.dataType
        FROM user_equipment_set_equipment u """)
    abstract fun loadUserEquipmentSetEquipment(): List<UserEquipmentEntity>

    @Query("""
        SELECT u.id, u.equipmentSetId, u.dataId, u.dataType
        FROM user_equipment_set_equipment u
        WHERE u.equipmentSetId = :equipmentSetId""")
    abstract fun loadUserEquipmentSetEquipment(equipmentSetId: Int): List<UserEquipmentEntity>

    @Query("""
        SELECT u.id, u.dataId, u.decorationId, u.dataType, u.decorationId, u.equipmentSetId, u.slotNumber
        FROM user_equipment_set_decorations u """)
    abstract fun loadUserEquipmentDecorations(): List<UserEquipmentDecorationEntity>

    @Query("""
        SELECT u.id, u.dataId, u.decorationId, u.dataType, u.decorationId, u.equipmentSetId, u.slotNumber
        FROM user_equipment_set_decorations u
        WHERE u.equipmentSetId = :equipmentSetId""")
    abstract fun loadUserEquipmentDecorations(equipmentSetId: Int): List<UserEquipmentDecorationEntity>

    @Query("""
        DELETE FROM user_equipment_sets
        WHERE id = :equipmentSetId
    """)
    abstract fun deleteUserEquipmentSet(equipmentSetId: Int)

    fun loadUserEquipmentSetIds(): List<UserEquipmentSetIds> {
        val set = loadUserEquipmentSetInfo()
        val equipment = loadUserEquipmentSetEquipment()
        val decorations = loadUserEquipmentDecorations()

        return set.map {
            UserEquipmentSetIds(it.id, it.name, equipment.filter { userEquipmentEntity ->
                userEquipmentEntity.equipmentSetId == it.id
            }.map { equipment ->
                UserEquipmentIds(equipment.dataId, equipment.equipmentSetId, equipment.dataType,
                        decorations.filter { decoration ->
                            decoration.dataId == equipment.dataId && decoration.dataType == equipment.dataType && decoration.equipmentSetId == it.id
                        }.map { decoration -> UserDecorationIds(decoration.decorationId, decoration.slotNumber) }
                                .toMutableList())
            }.toMutableList())
        }
    }

    fun loadUserEquipmentSetIds(equipmentSetId: Int): UserEquipmentSetIds {
        val set = loadUserEquipmentSetInfo(equipmentSetId)
        val equipment = loadUserEquipmentSetEquipment(equipmentSetId)
        val decorations = loadUserEquipmentDecorations(equipmentSetId)

        return UserEquipmentSetIds(set.id, set.name, equipment.map { equipment ->
            UserEquipmentIds(equipment.dataId, equipment.equipmentSetId, equipment.dataType,
                    decorations.filter { decoration ->
                        decoration.dataId == equipment.dataId && decoration.dataType == equipment.dataType
                    }.map { decoration -> UserDecorationIds(decoration.decorationId, decoration.slotNumber) }
                            .toMutableList())
        }.toMutableList())
    }

    @Query("""INSERT INTO user_equipment_sets VALUES (NULL, :name)""")
    abstract fun createUserEquipmentSet(name: String) : Long

    @Query("""INSERT INTO user_equipment_set_equipment VALUES (NULL, :id, :type, :equipmentSetId )""")
    abstract fun createUserEquipmentEquipment(id: Int, type: DataType, equipmentSetId: Int)

    @Query("""DELETE FROM user_equipment_set_equipment WHERE dataId = :dataId AND dataType = :type AND equipmentSetId = :equipmentSetId""")
    abstract fun deleteUserEquipmentEquipment(dataId: Int, type: DataType, equipmentSetId: Int)

    @Query("""DELETE FROM user_equipment_set_equipment """)
    abstract fun deleteUserEquipmentEquipment()

    @Query("""INSERT INTO user_equipment_set_decorations VALUES (NULL, :equipmentSetId, :dataId, :dataType, :decorationId, :slotNumber)""")
    abstract fun createUserEquipmentDecoration(equipmentSetId: Int, dataId: Int, dataType: DataType, decorationId: Int, slotNumber: Int)
}
