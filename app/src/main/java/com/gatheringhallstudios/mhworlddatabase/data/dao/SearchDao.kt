package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.util.CachedValue
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.features.search.SearchFilter

// values are cached for 30 seconds
val timeout: Long = 30 * 1000

@Dao
abstract class SearchDao {

    private val locationDataCache = CachedValue(timeout) {
        loadAllLocationsSync(AppSettings.dataLocale)
    }

    private val monsterDataCache = CachedValue(timeout) {
        loadAllMonstersSync(AppSettings.dataLocale)
    }

    private val skillTreeDataCache = CachedValue(timeout) {
        loadAllSkilltreesSync(AppSettings.dataLocale)
    }

    private val charmDataCache = CachedValue(timeout) {
        loadAllCharmsSync(AppSettings.dataLocale)
    }

    private val decorationDataCache = CachedValue(timeout) {
        loadAllDecorationsSync(AppSettings.dataLocale)
    }

    private val armorDataCache = CachedValue(timeout) {
        loadAllArmorSync(AppSettings.dataLocale)
    }

    private val itemDataCache = CachedValue(timeout) {
        loadAllItemsSync(AppSettings.dataLocale)
    }

    private val weaponDataCache = CachedValue(timeout) {
        loadAllWeaponsSync(AppSettings.dataLocale)
    }

    private val questDataCache = CachedValue(timeout) {
        loadAllQuestsSync(AppSettings.dataLocale)
    }

    private val kinsectDataCache = CachedValue(timeout) {
        loadAllKinsectsSync(AppSettings.dataLocale)
    }

    fun searchLocations(searchFilter: String): List<Location> {
        val filter = SearchFilter(searchFilter)
        return locationDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchMonsters(searchFilter: String): List<MonsterBase> {
        val filter = SearchFilter(searchFilter)
        return monsterDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchSkillTrees(searchFilter: String): List<SkillTreeBase> {
        val filter = SearchFilter(searchFilter)
        return skillTreeDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchCharms(searchFilter: String): List<Charm> {
        val filter = SearchFilter(searchFilter)
        return charmDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchDecorations(searchFilter: String): List<DecorationBase> {
        val filter = SearchFilter(searchFilter)
        return decorationDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchArmor(searchFilter: String): List<ArmorBase> {
        val filter = SearchFilter(searchFilter)
        return armorDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchItems(searchFilter: String): List<ItemBase> {
        val filter = SearchFilter(searchFilter)
        return itemDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchWeapons(searchFilter: String): List<WeaponBase> {
        val filter = SearchFilter(searchFilter)
        return weaponDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchQuests(searchFilter: String): List<QuestBase> {
        val filter = SearchFilter(searchFilter)
        return questDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchKinsects(searchFilter: String): List<Kinsect> {
        val filter = SearchFilter(searchFilter)
        return kinsectDataCache.get().filter { filter.matches(it.name) }
    }

    // All queries for search are below. Currently, it loads the entire table.
    // These are basically sync duplicates of existing queries, but the slower ones
    // could be replaced for an "in-db" search, once the db supports normalized_names

    @Query("""
        SELECT id, name
        FROM location_text
        WHERE lang_id = :langId
        ORDER BY name ASC
    """)
    protected abstract fun loadAllLocationsSync(langId: String): List<Location>

    @Query("""
        SELECT m.id, mt.name, m.size, mt.ecology
        FROM monster m
            JOIN monster_text mt USING (id)
        WHERE mt.lang_id = :langId
        ORDER BY mt.name ASC""")
    protected abstract fun loadAllMonstersSync(langId: String): List<MonsterBase>

    @Query("""
        SELECT s.id, st.name, s.max_level, s.icon_color, s.secret, s.unlocks_id
        FROM skilltree s
            JOIN skilltree_text st USING (id)
        WHERE st.lang_id = :langId
        ORDER BY st.name ASC
    """)
    protected abstract fun loadAllSkilltreesSync(langId: String): List<SkillTreeBase>

    @Query("""
        SELECT d.id, dt.name, d.slot, d.icon_color
        FROM decoration d
            JOIN decoration_text dt USING (id)
        WHERE dt.lang_id = :langId
        ORDER BY dt.name ASC
    """)
    protected abstract fun loadAllDecorationsSync(langId: String): List<DecorationBase>

    @Query("""
        SELECT c.id, c.previous_id, c.rarity, ct.name
        FROM charm c
            JOIN charm_text ct USING (id)
        WHERE ct.lang_id = :langId
        ORDER BY ct.name ASC
    """)
    protected abstract fun loadAllCharmsSync(langId: String): List<Charm>

    @Query("""
        SELECT a.id, at.name, a.rarity, a.rank, a.armor_type, a.slot_1, a.slot_2, a.slot_3
        FROM armor a
            JOIN armor_text at USING (id)
        WHERE at.lang_id = :langId
        ORDER BY at.name ASC
    """)
    protected abstract fun loadAllArmorSync(langId: String): List<ArmorBase>

    @Query("""
        SELECT i.id, itext.name, i.icon_name, i.category, i.icon_color
        FROM item i
            JOIN item_text itext USING (id)
        WHERE itext.lang_id = :langId
        ORDER BY itext.name ASC
    """)
    protected abstract fun loadAllItemsSync(langId: String): List<ItemBase>

    @Query("""
        SELECT w.id, w.rarity, w.weapon_type, w.category, w.attack, w.affinity, w.previous_weapon_id,
        w.slot_1, w.slot_2, w.slot_3, w.element_hidden, wt.*
        FROM weapon w
            JOIN weapon_text wt USING (id)
        WHERE wt.lang_id = :langId
    """)
    abstract fun loadAllWeaponsSync(langId: String): List<WeaponBase>

    @Query("""
        SELECT q.id, q.category, q.stars, qt.name, q.quest_type, 
            qt.objective, qt.description, q.location_id, q.zenny
        FROM quest q
            JOIN quest_text qt USING (id)
        WHERE qt.lang_id = :langId
        ORDER BY q.order_id ASC
    """)
    abstract fun loadAllQuestsSync(langId: String): List<QuestBase>

    @Query("""
        SELECT k.id, kt.name, k.rarity, k.previous_kinsect_id,
            k.attack_type, k.dust_effect, k.power, k.speed, k.heal
        FROM kinsect k
            JOIN kinsect_text kt USING (id)
        WHERE kt.lang_id = :langId
        ORDER BY kt.name ASC
    """)
    abstract fun loadAllKinsectsSync(langId: String): List<Kinsect>
}