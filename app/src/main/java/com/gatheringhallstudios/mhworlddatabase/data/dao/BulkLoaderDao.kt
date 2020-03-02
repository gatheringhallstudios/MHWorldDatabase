package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.util.createLiveData

@Dao
abstract class BulkLoaderDao {

    fun getModels(langId: String,
                  armorIds: IntArray,
                  charmIds: IntArray,
                  itemIds: IntArray,
                  locationIds: IntArray,
                  monsterIds: IntArray,
                  skillTreeIds: IntArray,
                  weaponIds: IntArray,
                  decorationIds: IntArray): LiveData<BulkModels> = createLiveData {
        BulkModels(
                armor = loadArmorByIdList(langId, armorIds),
                items = loadItemsByIdList(langId, itemIds),
                charms = loadCharmsByIdList(langId, charmIds),
                locations = loadLocationsByIdList(langId, locationIds),
                monsters = loadMonstersByIdList(langId, monsterIds),
                skillTrees = loadSkillTreesByIdList(langId, skillTreeIds),
                weapons = loadWeaponsByIdList(langId, weaponIds),
                decorations = loadDecorationsByIdList(langId, decorationIds)
        )
    }

    @Query("""
        SELECT a.*, at.name, ast.name armorset_name
        FROM armor a
            JOIN armor_text at USING (id)
            JOIN armorset_text ast
                ON ast.id = a.armorset_id
                AND ast.lang_id = at.lang_id
        WHERE at.lang_id = :langId
        AND a.id IN (:armorIds)
    """)
    abstract fun loadArmorByIdList(langId: String, armorIds: IntArray) : List<Armor>

    @Query("""
        SELECT i.*, it.name, it.description, i.category
        FROM item i
            JOIN item_text it
                ON it.id = i.id
                AND it.lang_id = :langId
        WHERE i.id IN (:itemIds)
        ORDER BY i.id""")
    abstract fun loadItemsByIdList(langId: String, itemIds: IntArray): List<Item>

    @Query("""
        SELECT c.*, ct.name
        FROM charm c
            JOIN charm_text ct
                ON ct.id = c.id
                AND ct.lang_id = :langId
        WHERE c.id IN (:charmIds)
        ORDER BY ct.name""")
    abstract fun loadCharmsByIdList(langId: String, charmIds: IntArray): List<Charm>

    @Query("""
        SELECT d.id, dt.name, d.slot, d.icon_color
        FROM decoration d
            JOIN decoration_text dt
                ON dt.id = d.id
                AND dt.lang_id = :langId
        WHERE d.id IN (:decorationIds)
        ORDER BY dt.name""")
    abstract fun loadDecorationsByIdList(langId: String, decorationIds: IntArray): List<DecorationBase>

    @Query("""
        SELECT id, name
        FROM location_text t
        WHERE t.lang_id = :langId
        AND id IN (:locationIds)
        ORDER BY order_id ASC
        """)
    abstract fun loadLocationsByIdList(langId: String, locationIds: IntArray): List<Location>

    @Query("""
        SELECT m.id, m.size, t.name, t.ecology
        from monster m JOIN monster_text t USING (id)
        WHERE t.lang_id = :langId
          AND m.id IN (:monsterIds)
        ORDER BY t.name ASC""")
    abstract fun loadMonstersByIdList(langId: String, monsterIds: IntArray): List<MonsterBase>

    @Query("""
        SELECT id, name, max_level, description, icon_color, secret, unlocks_id
        FROM skilltree s join skilltree_text st USING (id)
        WHERE lang_id = :langId
        AND id IN (:skillTreeIds)
        ORDER BY name """)
    abstract fun loadSkillTreesByIdList(langId: String, skillTreeIds: IntArray): List<SkillTree>

    @Query("""
        SELECT w.id, w.weapon_type, w.category, w.rarity, w.attack, w.attack_true, w.affinity, w.defense, w.slot_1, w.slot_2, w.slot_3, w.element1, w.element1_attack,
            w.element2, w.element2_attack, w.element_hidden, w.sharpness, w.sharpness_maxed, w.previous_weapon_id, w.craftable, w.kinsect_bonus,
            w.elderseal, w.phial, w.phial_power, w.shelling, w.shelling_level, w.coating_close, w.coating_power, w.coating_poison, w.coating_paralysis, w.coating_sleep, w.coating_blast,
            w.notes, wa.special_ammo, wt.name
        FROM weapon w
            JOIN weapon_text wt USING (id)
            LEFT JOIN weapon_ammo wa ON w.ammo_id = wa.id
        WHERE wt.lang_id = :langId
            AND w.id IN (:weaponIds)
        ORDER BY w.id ASC
          """)
    abstract fun loadWeaponsByIdList(langId: String, weaponIds: IntArray): List<Weapon>
}