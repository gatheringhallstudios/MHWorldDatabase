package com.gatheringhallstudios.mhworlddatabase.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Defines an interface to query information.
 * TODO: Probably split it later if it gets too big
 * Created by Carlos on 3/4/2018.
 */
@Dao
public abstract class MHWDao {
    @Query( "SELECT m.*, t.name, t.description " +
            "from monster m JOIN monster_text t USING (id) " +
            "WHERE t.lang_id = :langId " +
            "ORDER BY t.name ASC")
    public abstract LiveData<List<Monster>> loadMonsterList(String langId);

    @Query( "SELECT id, name, description " +
            "FROM skilltree join skilltree_text USING (id) " +
            "WHERE lang_id = :langId " +
            "ORDER BY name ASC")
    public abstract LiveData<List<SkillTreeBasic>> loadSkillTreeList(String langId);

    @Query( "SELECT st.id, stt.name, stt.description, s.skilltree_id, s.level, s.description " +
            "FROM skilltree st " +
            "   JOIN skilltree_text stt " +
            "       ON stt.id = st.id " +
            "       AND stt.lang_id = :langId " +
            "   JOIN skill s" +
            "       ON s.skilltree_id = st.id " +
            "       AND s.lang_id = :langId " +
            "WHERE st.id = :skillTreeId ")
    public abstract LiveData<SkillTree> loadSkill(String langId, int skillTreeId);

    @Query( "SELECT id, name, rarity, armor_type type " +
            "FROM armor JOIN armor_text USING (id)" +
            "WHERE lang_id = :langId " +
            "   AND rarity BETWEEN :minRarity AND :maxRarity " +
            "ORDER BY id ASC")
    public abstract LiveData<List<ArmorBasic>> loadArmorList(String langId, int minRarity, int maxRarity);

    public LiveData<List<ArmorBasic>> loadArmorList(String langId) {
        return loadArmorList(langId, 0, 999);
    }

    @Query( "SELECT a.*, at.name " +
            "FROM armor a JOIN armor_text at USING (id) " +
            "WHERE at.lang_id = :langId " +
            "AND a.id = :armorId")
    public abstract LiveData<Armor> loadArmor(String langId, int armorId);
}
