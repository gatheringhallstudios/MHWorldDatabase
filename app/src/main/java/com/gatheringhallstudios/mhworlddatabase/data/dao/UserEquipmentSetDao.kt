package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.room.*
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentDecorationEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentSetEntity
import com.gatheringhallstudios.mhworlddatabase.data.models.UserDecoration
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipment
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet

@Dao
abstract class UserEquipmentSetDao {
    @Query("""
        SELECT u.id, u.dataId, u.dataType
        FROM user_equipment_sets u """)
    abstract fun loadUserEquipment(): List<UserEquipmentSetEntity>

    @Query("""
        SELECT u.id, u.dataId, u.decorationId, u.dataType, u.decorationId, u.equipmentSetId
        FROM user_equipment_set_decorations u """)
    abstract fun loadUserEquipmentDecorations(): List<UserEquipmentDecorationEntity>

    fun loadUserEquipmentSets(): List<UserEquipmentSet> {
        val equipment = loadUserEquipment()
        val decorations = loadUserEquipmentDecorations()

        return equipment.map {
            UserEquipment(it.dataId, it.id, it.dataType,
                    decorations.filter { itr -> itr.dataId == it.dataId && itr.dataType == it.dataType && itr.equipmentSetId == it.id }
                            .map { decoration -> UserDecoration(decoration.decorationId) }
                            .toMutableList())
        }.groupBy { element -> element.equipmentSetId }.map {
            UserEquipmentSet(it.value.first().equipmentSetId, it.value.toMutableList())
        }
    }


//    @Query("""
//
//    """)
//    abstract fun insert(entities: BookmarkEntity)

//    @Delete
//    abstract fun delete(entities: BookmarkEntity)
}
