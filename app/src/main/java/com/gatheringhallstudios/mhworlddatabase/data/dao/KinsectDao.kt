package com.gatheringhallstudios.mhworlddatabase.data.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuantity

import com.gatheringhallstudios.mhworlddatabase.data.models.Kinsect;
import com.gatheringhallstudios.mhworlddatabase.data.models.KinsectFull
import com.gatheringhallstudios.mhworlddatabase.data.models.MHModelTree

@Dao
abstract class KinsectDao {

    @Query("""
        SELECT k.id, kt.name, k.rarity, k.previous_kinsect_id, k.attack_type, k.dust_effect, k.power, k.speed, k.heal
        FROM kinsect k
            JOIN kinsect_text kt USING (id)
        WHERE kt.lang_id = :langId
            AND k.id = :kinsectId
        ORDER BY k.id ASC""")
    abstract fun loadKinsectSync(langId: String, kinsectId: Int): Kinsect

    @Query("""
        SELECT i.id item_id, it.name item_name, i.icon_name item_icon_name,
                    i.category item_category, i.icon_color item_icon_color, ri.quantity
        FROM
        (
            SELECT item_id, quantity
            FROM recipe_item
            WHERE recipe_id = (SELECT recipe_id FROM kinsect WHERE id = :kinsectId)
        ) ri
        JOIN item i
          ON i.id = ri.item_id
        JOIN item_text it
          ON it.id = i.id
          AND it.lang_id = :langId
        ORDER BY i.id
    """)
    abstract fun loadKinsectComponents(langId: String, kinsectId: Int): List<ItemQuantity>

    @Query("""
        SELECT k.id, kt.name, k.rarity, k.previous_kinsect_id, k.attack_type, k.dust_effect, k.power, k.speed, k.heal
        FROM kinsect k
            JOIN kinsect_text kt
                ON kt.id = k.id
                AND kt.lang_id = :langId
        ORDER BY k.id ASC""")
    abstract fun loadKinsectsSync(langId: String): List<Kinsect>

    fun loadKinsectFullSync(langId: String, kinsectId: Int): KinsectFull {
        val kinsect = loadKinsectSync(langId, kinsectId)
        return KinsectFull(
                kinsect = kinsect,
                recipe = queryRecipeComponents(langId, kinsectId)
        )
    }

    fun queryRecipeComponents(langId: String, kinsectId: Int): List<ItemQuantity> {
        return loadKinsectComponents(langId, kinsectId)
    }

    fun loadKinsectTrees(langId: String): MHModelTree<Kinsect> {
        return MHModelTree(loadKinsectsSync(langId))
    }
}
