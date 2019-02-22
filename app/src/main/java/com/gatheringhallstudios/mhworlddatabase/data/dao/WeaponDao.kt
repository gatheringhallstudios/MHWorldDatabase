package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.*

import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.util.createLiveData


@Dao
abstract class WeaponDao {

    /**
     * Loads all weapons for a provided weapon type
     */
    @Query("""
        SELECT w.id, w.weapon_type, w.rarity, w.attack, w.attack_true, w.affinity, w.defense, w.slot_1, w.slot_2, w.slot_3, w.element1, w.element1_attack,
            w.element2, w.element2_attack, w.element_hidden, w.sharpness, w.sharpness_maxed, w.previous_weapon_id, w.craftable, w.kinsect_bonus,
            w.elderseal, w.phial, w.phial_power, w.shelling, w.shelling_level, w.coating_close, w.coating_power, w.coating_poison, w.coating_paralysis, w.coating_sleep, w.coating_blast,
            w.notes, wa.special_ammo, wt.name
        FROM weapon w
            JOIN weapon_text wt USING (id)
            LEFT JOIN weapon_ammo wa ON w.ammo_id = wa.id
        WHERE wt.lang_id = :langId
            AND w.weapon_type = :weaponType
        ORDER BY w.id ASC
          """)
    abstract fun loadWeapons(langId: String, weaponType: WeaponType): List<Weapon>

    /**
     * Loads a single weapon by id
     */
    @Query("""
        SELECT w.id, w.weapon_type, w.rarity, w.attack, w.attack_true, w.affinity, w.defense, w.slot_1, w.slot_2, w.slot_3, w.element1, w.element1_attack,
            w.element2, w.element2_attack, w.element_hidden, w.sharpness, w.sharpness_maxed, w.previous_weapon_id, w.craftable, w.kinsect_bonus,
            w.elderseal, w.phial, w.phial_power, w.shelling, w.shelling_level, w.coating_close, w.coating_power, w.coating_poison, w.coating_paralysis, w.coating_sleep, w.coating_blast,
            w.notes, wt.name
        FROM weapon w
            JOIN weapon_text wt USING (id)
            LEFT OUTER JOIN weapon_ammo wa ON w.ammo_id = wa.id
        WHERE w.id = :weaponId
        AND wt.lang_id = :langId
        """)
    abstract fun loadWeapon(langId: String, weaponId: Int): Weapon

    @Query("""
        SELECT i.id item_id, it.name item_name, i.icon_name item_icon_name,
            i.category item_category, i.icon_color item_icon_color, w.quantity, w.recipe_type
         FROM weapon_recipe w
            JOIN item i
                ON w.item_id = i.id
            JOIN item_text it
                ON it.id = i.id
                AND it.lang_id = :langId
        WHERE it.lang_id = :langId
        AND w.weapon_id= :weaponId
        ORDER BY i.id
    """)
    abstract fun loadWeaponComponents(langId: String, weaponId: Int): List<ItemQuantity>

    /**
     * Loads the WeaponAmmoData for a weapon. If the weapon has no data (not a bowgun) returns null.
     */
    @Query("""
        SELECT wa.id AS ammo_id, wa.deviation, wa.special_ammo, wa.normal1_clip, wa.normal1_rapid, wa.normal1_recoil, wa.normal1_reload, wa.normal2_clip, wa.normal2_rapid,
        wa.normal2_recoil, wa.normal2_reload, wa.normal3_clip, wa.normal3_rapid, wa.normal3_recoil, wa.normal3_reload, wa.pierce1_clip, wa.pierce1_rapid, wa.pierce1_recoil, wa.pierce1_reload,
        wa.pierce2_clip, wa.pierce2_rapid, wa.pierce2_recoil, wa.pierce2_reload, wa.pierce3_clip, wa.pierce3_rapid, wa.pierce3_recoil, wa.pierce3_reload, wa.spread1_clip, wa.spread1_rapid,
        wa.spread1_recoil, wa.spread1_reload, wa.spread2_clip, wa.spread2_rapid, wa.spread2_recoil, wa.spread2_reload, wa.spread3_clip, wa.spread3_rapid, wa.spread3_recoil, wa.spread3_reload,
        wa.sticky1_clip, wa.sticky1_rapid, wa.sticky1_recoil, wa.sticky1_reload, wa.sticky2_clip, wa.sticky2_rapid, wa.sticky2_recoil, wa.sticky2_reload, wa.sticky3_clip, wa.sticky3_rapid, wa.sticky3_recoil, wa.sticky3_reload,
        wa.cluster1_clip, wa.cluster1_rapid, wa.cluster1_recoil, wa.cluster1_reload, wa.cluster2_clip, wa.cluster2_rapid, wa.cluster2_recoil, wa.cluster2_reload, wa.cluster3_clip, wa.cluster3_rapid, wa.cluster3_recoil, wa.cluster3_reload,
        wa.recover1_clip, wa.recover1_rapid, wa.recover1_recoil, wa.recover1_reload, wa.recover2_clip, wa.recover2_rapid, wa.recover2_recoil, wa.recover2_reload, wa.poison1_clip, wa.poison1_rapid, wa.poison1_recoil, wa.poison1_reload,
        wa.poison2_clip, wa.poison2_rapid, wa.poison2_recoil, wa.poison2_reload, wa.paralysis1_clip, wa.paralysis1_rapid, wa.paralysis1_recoil, wa.paralysis1_reload, wa.paralysis2_clip, wa.paralysis2_rapid, wa.paralysis2_recoil, wa.paralysis2_reload,
        wa.sleep1_clip, wa.sleep1_rapid, wa.sleep1_recoil, wa.sleep1_reload, wa.sleep2_clip, wa.sleep2_rapid, wa.sleep2_recoil, wa.sleep2_reload, wa.exhaust1_clip, wa.exhaust1_rapid, wa.exhaust1_recoil, wa.exhaust1_reload,
        wa.exhaust2_clip, wa.exhaust2_rapid, wa.exhaust2_recoil, wa.exhaust2_reload, wa.flaming_clip, wa.flaming_rapid, wa.flaming_recoil, wa.flaming_reload, wa.water_clip, wa.water_rapid, wa.water_recoil, wa.water_reload,
        wa.freeze_clip, wa.freeze_rapid, wa.freeze_recoil, wa.freeze_reload, wa.thunder_clip, wa.thunder_rapid, wa.thunder_recoil, wa.thunder_reload, wa.dragon_clip, wa.dragon_rapid, wa.dragon_recoil, wa.dragon_reload,
        wa.slicing_clip, wa.slicing_rapid, wa.slicing_recoil, wa.slicing_reload, wa.wyvern_clip, wa.wyvern_reload, wa.demon_clip, wa.demon_recoil, wa.demon_reload, wa.armor_clip, wa.armor_recoil, wa.armor_reload, wa.tranq_clip, wa.tranq_recoil, wa.tranq_reload

        FROM weapon w
            JOIN weapon_text wt USING (id)
            LEFT OUTER JOIN weapon_ammo wa ON w.ammo_id = wa.id
        WHERE w.id = :weaponId
        AND wt.lang_id = :langId
    """)
    abstract fun loadWeaponAmmoData(langId: String, weaponId: Int): WeaponAmmoData?

    /**
     * Loads all weapon melodies from the database. The reason for this is that it is very difficult
     * to search for all permutations of hunting horn notes from the database. The note -> melody
     * matching will be done in Kotlin instead
     */
    @Query("""
        SELECT wm.id, wm.notes, wmt.effect1, wmt.effect2, wm.duration, wm.extension
         FROM weapon_melody wm
            JOIN weapon_melody_text wmt
                ON  wm.id = wmt.id
        WHERE wmt.lang_id = :langId
        ORDER BY wm.id
    """)
    abstract fun loadWeaponMelodies(langId: String): List<WeaponMelody>

    fun queryRecipeComponents(langId: String, weaponId: Int): Map<String?, List<ItemQuantity>> {
        return loadWeaponComponents(langId, weaponId).groupBy { it.recipe_type }
    }

    fun queryWeaponMelodies(langId: String, weapon: Weapon): List<WeaponMelody> {
        if (weapon.weapon_type != WeaponType.HUNTING_HORN) return listOf()
        val comparator = Array(4) {
            if (it < weapon.notes!!.length) {
                Character.getNumericValue(weapon.notes[it])
            } else {
                0
            }
        }

        return loadWeaponMelodies(langId).filter {
            var result = true
            for (note in it.notes!!) {
                if (!comparator.contains(Character.getNumericValue(note))) {
                    result = false
                }
            }

            result
        }
    }

    @Query("""
        SELECT s.id skill_id, stt.name skill_name, s.max_level skill_max_level, s.icon_color skill_icon_color,
            wskill.level level
        FROM weapon_skill wskill
            JOIN weapon w
                ON wskill.weapon_id = w.id
            JOIN skilltree s
                ON wskill.skilltree_id = s.id
            JOIN skilltree_text stt
                ON wskill.skilltree_id = stt.id
            WHERE stt.lang_id = :langId
               AND wskill.weapon_id = :weaponId
            ORDER BY wskill.skilltree_id ASC""")
    abstract fun queryWeaponSkillsSync(langId: String, weaponId: Int): List<SkillLevel>

    /**
     * Loads extended detail weapon for a particular weapon.
     * Equivalent to calling loadWeapon with additional bundled information.
     */
    fun loadWeaponFullSync(langId: String, weaponId: Int): WeaponFull {
        val weapon = loadWeapon(langId, weaponId)
        return WeaponFull(
                weapon = weapon,
                recipe = queryRecipeComponents(langId, weaponId),
                ammo = loadWeaponAmmoData(langId, weaponId),
                melodies = queryWeaponMelodies(langId, weapon),
                skills = queryWeaponSkillsSync(langId, weaponId)
        )
    }

    /**
     * Loads all weapons of a particular type, contained as a collection of weapon trees.
     */
    fun loadWeaponTrees(langId: String, weaponType: WeaponType): MHModelTree<Weapon> {
        // todo: cache it
        return MHModelTree(loadWeapons(langId, weaponType))
    }
}
