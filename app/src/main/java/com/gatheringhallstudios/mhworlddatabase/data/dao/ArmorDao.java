package com.gatheringhallstudios.mhworlddatabase.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.gatheringhallstudios.mhworlddatabase.data.Armor;
import com.gatheringhallstudios.mhworlddatabase.data.ArmorBasic;

import java.util.List;

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
public abstract class ArmorDao {
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
