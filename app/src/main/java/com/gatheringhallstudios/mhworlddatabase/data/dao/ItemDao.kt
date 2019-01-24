package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.util.createLiveData


@Dao
abstract class ItemDao {
    @Query("""
        SELECT i.*, it.name, it.description, i.category
        FROM item i
            JOIN item_text it
                ON it.id = i.id
                AND it.lang_id = :langId
        WHERE (:category IS NULL AND i.category != 'hidden') OR i.category = :category
        ORDER BY i.id""")
    abstract fun loadItems(langId: String, category: ItemCategory? = null): LiveData<List<Item>>

    @Query("""
        SELECT i.*, it.name, it.description
        FROM item i
        JOIN item_text it
            ON it.id = i.id
            AND it.lang_id = :langId
        WHERE i.id = :itemId """)
    abstract fun loadItem(langId: String, itemId: Int): LiveData<Item>

    /**
     * Loads all possible gathering locations for an item synchronously
     */
    @Query("""
        SELECT li.location_id, lt.name location_name,
            li.rank, li.area, li.stack, li.percentage, li.nodes
        FROM location_item li
            JOIN location_text lt
                ON lt.id = li.location_id
        WHERE li.item_id = :itemId
          AND lt.lang_id = :langId
        """)
    abstract fun loadItemLocationsSync(langId: String, itemId: Int): List<ItemLocation>

    /**
     * Synchronously load all ways to get items from monsters
     */
    @Query("""
        SELECT r.monster_id, mtext.name monster_name, m.size monster_size,
            rtext.name condition_name, r.rank, r.stack, r.percentage
        FROM monster_reward r
            JOIN monster_reward_condition_text rtext
                ON rtext.id = r.condition_id
            JOIN monster m
                ON m.id = r.monster_id
            JOIN monster_text mtext
                ON mtext.id = m.id
                AND mtext.lang_id = rtext.lang_id
        WHERE r.item_id = :itemId
          AND rtext.lang_id = :langId
        ORDER BY m.id ASC, r.percentage DESC
    """)
    abstract fun loadItemRewardsSync(langId: String, itemId: Int): List<ItemReward>

    /**
     * Loads all possible gathering locations for an item asynchronously
     */
    fun loadItemLocations(langId: String, itemId: Int) = createLiveData {
        return@createLiveData loadItemLocationsSync(langId, itemId)
    }

    @Query("""
        SELECT c.id,
        r.id result_id, rt.name result_name, r.icon_name result_icon_name, r.icon_color result_icon_color, r.category result_category,
        f.id first_id, ft.name first_name, f.icon_name first_icon_name, f.icon_color first_icon_color, f.category first_category,
        s.id second_id, st.name second_name, s.icon_name second_icon_name, s.icon_color second_icon_color, s.category second_category,
        c.quantity quantity
        FROM item_combination c
            JOIN item r
                ON r.id = c.result_id
            JOIN item_text rt
                ON rt.id = r.id
                AND rt.lang_id = :langId
            JOIN item f
                ON f.id = c.first_id
            JOIN item_text ft
                ON ft.id = f.id
                AND ft.lang_id = :langId
            LEFT JOIN item s
                ON s.id = c.second_id
            LEFT JOIN item_text st
                ON st.id = s.id
                AND st.lang_id = :langId
        WHERE :itemId IS NULL
            OR c.result_id = :itemId
            OR c.first_id = :itemId
            OR c.second_id = :itemId
        ORDER BY c.id ASC""")
    abstract fun loadItemCombinationsBaseSync(langId: String, itemId: Int? = null): List<ItemCombination>

    /**
     * Returns all item combinations that exist asynchronously
     */
    fun loadItemCombinations(langId: String) = createLiveData {
        return@createLiveData loadItemCombinationsBaseSync(langId)
    }

    @Query("""
        SELECT c.id, c.rarity, c.previous_id, ctext.name, cr.quantity
        FROM charm_recipe cr
            JOIN charm c
                ON c.id = cr.charm_id
            JOIN charm_text ctext
                ON ctext.id = c.id
        WHERE cr.item_id = :itemId
          AND ctext.lang_id = :langId
    """)
    abstract fun loadCharmUsageForSync(langId: String, itemId: Int): List<ItemUsageCharm>

    @Query("""
        SELECT armor_id id, name, armor_type, rarity, slot_1, slot_2, slot_3, ar.quantity
        FROM armor_recipe ar
            JOIN armor a
                ON ar.armor_id = a.id
            JOIN armor_text atext
                ON a.id = atext.id
        WHERE ar.item_id = :itemId
          AND atext.lang_id = :langId
    """)
    abstract fun loadArmorUsageForSync(langId: String, itemId: Int): List<ItemUsageArmor>


    @Query("""
        SELECT w.id, w.rarity, w.weapon_type, w.attack, w.affinity, w.attack_true, w.affinity,
            w.element1, w.element1_attack, w.element1, w.element2_attack, w.element_hidden, w.defense,
            w.previous_weapon_id, w.craftable, w.kinsect_bonus, w.elderseal, w.phial, w.phial_power,
            w.shelling, w.shelling_level, w.notes, w.slot_1, w.slot_2, w.slot_3,
            wt.*, wr.quantity
        FROM weapon w
            JOIN weapon_text wt USING (id)
            JOIN weapon_recipe wr ON w.id = wr.weapon_id
        WHERE wt.lang_id = :langId
            AND wr.item_id = :itemId
    """)
    abstract fun loadWeaponUsageForSync(langId: String, itemId: Int): List<ItemUsageWeapon>

    /**
     * Loads all potential ways to use an item asynchronously
     */
    fun loadItemUsagesFor(langId: String, itemId: Int) = createLiveData {
        val itemCombos = loadItemCombinationsBaseSync(langId, itemId)

        return@createLiveData ItemUsages(
                craftRecipes = itemCombos.filter {
                    it.first.id == itemId || it.second?.id == itemId
                },
                charms = loadCharmUsageForSync(langId, itemId),
                armor = loadArmorUsageForSync(langId, itemId),
                weapon = loadWeaponUsageForSync(langId, itemId)
        )
    }

    /**
     * Loads all potential ways to acquire an item as a single object asynchronously
     */
    fun loadItemSourcesFor(langId: String, itemId: Int) = createLiveData {
        val itemCombos = loadItemCombinationsBaseSync(langId, itemId)

        return@createLiveData ItemSources(
                craftRecipes = itemCombos.filter { it.result.id == itemId },
                locations = loadItemLocationsSync(langId, itemId),
                rewards = loadItemRewardsSync(langId, itemId)
        )
    }
}
