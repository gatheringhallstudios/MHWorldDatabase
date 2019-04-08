package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.room.*
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentDecorationEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentSetEntity
import com.gatheringhallstudios.mhworlddatabase.data.models.UserDecoration
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipment
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet

@Dao
abstract class UserEquipmentSetDao {
    @Query("""
        SELECT u.id, u.name
        FROM user_equipment_sets u """)
    abstract fun loadUserEquipmentSetInfo(): List<UserEquipmentSetEntity>

    @Query("""
        SELECT u.id, u.equipmentSetId, u.dataId, u.dataType
        FROM user_equipment_set_equipment u """)
    abstract fun loadUserEquipmentSetEquipment(): List<UserEquipmentEntity>

    @Query("""
        SELECT u.id, u.dataId, u.decorationId, u.dataType, u.decorationId, u.equipmentSetId
        FROM user_equipment_set_decorations u """)
    abstract fun loadUserEquipmentDecorations(): List<UserEquipmentDecorationEntity>

    fun loadUserEquipmentSets(): List<UserEquipmentSet> {
        val set = loadUserEquipmentSetInfo()
        val equipment = loadUserEquipmentSetEquipment()
        val decorations = loadUserEquipmentDecorations()

        return set.map {
            UserEquipmentSet(it.id, it.name, equipment.map { equipment ->
                UserEquipment(equipment.dataId, equipment.equipmentSetId, equipment.dataType,
                        decorations.filter { decoration -> decoration.dataId == equipment.dataId && decoration.dataType == equipment.dataType && decoration.equipmentSetId == equipment.id }
                                .map { decoration -> UserDecoration(decoration.decorationId) }
                                .toMutableList())
            }.toMutableList())
        }
    }
}
