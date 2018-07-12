package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorEntity
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.Armor
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorView
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
abstract class ArmorDao {
    @Query("""
        SELECT a.*, at.name, ast.name armorset_name
        FROM armor a
            JOIN armor_text at USING (id)
            JOIN armorset_text ast
                ON ast.id = a.armorset_id
                AND ast.lang_id = at.lang_id
        WHERE at.lang_id = :langId
          AND (:rank IS NULL OR a.rank = :rank)
    """)
    abstract fun loadArmorList(langId: String, rank: Rank?): LiveData<List<ArmorView>>

    @Query("""
        SELECT a.*, at.name, ast.name armorset_name
        FROM armor a
            JOIN armor_text at USING (id)
            JOIN armorset_text ast
                ON ast.id = a.armorset_id
                AND ast.lang_id = at.lang_id
        WHERE at.lang_id = :langId
        AND a.id = :armorId""")
    abstract fun loadArmor(langId: String, armorId: Int): LiveData<Armor>

    /**
     * Generates a list of ArmorSets with embedded ArmorViews
     * Equivalent to loadArmorList and then grouping
     */
    fun loadArmorSets(langId: String, rank: Rank): LiveData<List<ArmorSetView>> {
        // Load raw view of Armor with Armor Set info
        val armorSets = loadArmorList(langId, rank)

        return Transformations.map(armorSets) { data ->
            // Create a map of armorset_id -> ArmorView
            val setToArmorMap = data.groupBy { it.armorset_id }

            val armorSetList = mutableListOf<ArmorSetView>()

            for (armorGroup in setToArmorMap) {
                val armorSetId = armorGroup.value.first().armorset_id
                val armorSetName = armorGroup.value.first().armorset_name
                val armorList = armorGroup.value

                armorSetList.add(ArmorSetView(armorSetId, armorSetName, armorList))
            }

            armorSetList
        }
    }
}
