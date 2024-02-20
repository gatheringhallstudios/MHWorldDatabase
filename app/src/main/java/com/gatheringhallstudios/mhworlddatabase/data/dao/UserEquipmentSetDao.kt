package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.room.Dao
import androidx.room.Query
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
        SELECT u.id, u.equipmentSetId, u.dataId, u.dataType, u.orderId
        FROM user_equipment_set_equipment u """)
    abstract fun loadUserEquipmentSetEquipment(): List<UserEquipmentEntity>

    @Query("""
        SELECT u.id, u.equipmentSetId, u.dataId, u.dataType, u.orderId
        FROM user_equipment_set_equipment u
        WHERE u.equipmentSetId = :equipmentSetId
        AND u.dataId = :dataId
        AND u.dataType = :type""")
    abstract fun loadUserEquipmentSetEquipment(equipmentSetId: Int, dataId: Int, type: DataType): UserEquipmentEntity?

    @Query("""
        SELECT u.id, u.equipmentSetId, u.dataId, u.dataType, u.orderId
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
        SELECT u.id, u.dataId, u.decorationId, u.dataType, u.decorationId, u.equipmentSetId, u.slotNumber
        FROM user_equipment_set_decorations u
        WHERE u.equipmentSetId = :equipmentSetId AND
            u.dataId = :dataId """)
    abstract fun loadUserEquipmentDecorations(equipmentSetId: Int, dataId: Int): List<UserEquipmentDecorationEntity>

    @Query("""
        DELETE FROM user_equipment_sets
        WHERE id = :equipmentSetId
    """)
    abstract fun deleteUserEquipmentSet(equipmentSetId: Int)

    @Query("""
        DELETE FROM user_equipment_set_equipment
        WHERE equipmentSetId = :equipmentSetId
    """)
    abstract fun deleteUserEquipmentSetEquipment(equipmentSetId: Int)

    @Query("""
        DELETE FROM user_equipment_set_decorations
        WHERE equipmentSetId = :equipmentSetId
    """)
    abstract fun deleteUserEquipmentSetDecorations(equipmentSetId: Int)

    @Query("""INSERT INTO user_equipment_sets (id, name) VALUES (NULL, :name)""")
    abstract fun createUserEquipmentSet(name: String): Long

    @Query("""UPDATE user_equipment_sets SET name=:name WHERE id = :equipmentSetId""")
    abstract fun renameUserEquipmentSet(name: String, equipmentSetId: Int)

    @Query("""INSERT INTO user_equipment_set_equipment (id, dataId, dataType, equipmentSetID, orderId) VALUES (NULL, :id, :type, :equipmentSetId, :orderId )""")
    abstract fun createUserEquipmentEquipment(id: Int, type: DataType, equipmentSetId: Int, orderId: Int)

    @Query("""DELETE FROM user_equipment_set_equipment WHERE dataId = :dataId AND dataType = :type AND equipmentSetId = :equipmentSetId""")
    abstract fun deleteUserEquipmentEquipment(dataId: Int, type: DataType, equipmentSetId: Int)

    @Query("""INSERT INTO user_equipment_set_decorations (id, equipmentSetId, dataId, dataType, decorationId, slotNumber) VALUES (NULL, :equipmentSetId, :dataId, :dataType, :decorationId, :slotNumber)""")
    abstract fun createUserEquipmentDecoration(equipmentSetId: Int, dataId: Int, slotNumber: Int, dataType: DataType, decorationId: Int)

    @Query("""DELETE FROM user_equipment_set_decorations WHERE dataId = :dataId AND dataType = :type AND equipmentSetId = :equipmentSetId AND decorationId = :decorationId AND slotNumber = :targetSlot""")
    abstract fun deleteUserEquipmentDecoration(equipmentSetId: Int, dataId: Int, type: DataType, decorationId: Int, targetSlot: Int)

    @Query("""DELETE FROM user_equipment_set_decorations WHERE dataId = :dataId AND dataType = :type AND equipmentSetId = :equipmentSetId""")
    abstract fun deleteUserEquipmentDecorations(equipmentSetId: Int, dataId: Int, type: DataType)

    fun loadUserEquipmentSetIds(): List<UserEquipmentSetIds> {
        val set = loadUserEquipmentSetInfo()
        val equipment = loadUserEquipmentSetEquipment()
        val decorations = loadUserEquipmentDecorations()


        return set.map {
            UserEquipmentSetIds(it.id, it.name, equipment.filter { userEquipmentEntity ->
                userEquipmentEntity.equipmentSetId == it.id //Find all of the equipment entities belonging to this set
            }.map { equipment ->
                //Convert all of the equipment entities into UserEquipmentIds by including the decoration Ids that have been associated with this piece of equipment
                UserEquipmentIds(
                        dataId = equipment.dataId,
                        equipmentSetId = equipment.equipmentSetId,
                        dataType = equipment.dataType,
                        orderId = equipment.orderId,
                        decorationIds = decorations.filter { decoration ->
                            decoration.dataId == equipment.dataId
                                    && decoration.dataType == equipment.dataType
                                    && decoration.equipmentSetId == it.id
                        }.map { decoration -> UserDecorationIds(decoration.decorationId, decoration.slotNumber) }
                                .toMutableList())
            }.toMutableList())
        }
    }

    fun loadUserEquipmentSetIds(equipmentSetId: Int): UserEquipmentSetIds {
        val set = loadUserEquipmentSetInfo(equipmentSetId)
        val equipment = loadUserEquipmentSetEquipment(equipmentSetId)
        val decorations = loadUserEquipmentDecorations(equipmentSetId)

        return UserEquipmentSetIds(set.id, set.name, equipment.map { equipmentEntity ->
            UserEquipmentIds(
                    dataId = equipmentEntity.dataId,
                    equipmentSetId = equipmentEntity.equipmentSetId,
                    dataType = equipmentEntity.dataType,
                    orderId = equipmentEntity.orderId,
                    decorationIds = decorations.filter { decoration ->
                        decoration.dataId == equipmentEntity.dataId && decoration.dataType == equipmentEntity.dataType
                    }.map { decoration -> UserDecorationIds(decoration.decorationId, decoration.slotNumber) }
                            .toMutableList())
        }.toMutableList())
    }

    fun loadSingleUserEquipmentId(equipmentSetId: Int, equipmentId: Int, type: DataType): UserEquipmentIds? {
        val equipment = loadUserEquipmentSetEquipment(equipmentSetId, equipmentId, type)
        val decorations = loadUserEquipmentDecorations(equipmentSetId, equipmentId)

        return if (equipment == null) {
            null
        } else {
            UserEquipmentIds(
                    dataId = equipment.dataId,
                    dataType = equipment.dataType,
                    orderId = equipment.orderId,
                    decorationIds = decorations.map { decoration -> UserDecorationIds(decoration.decorationId, decoration.slotNumber) }.toMutableList(),
                    equipmentSetId = equipment.equipmentSetId
            )
        }
    }

}
