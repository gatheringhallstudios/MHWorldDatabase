package com.gatheringhallstudios.mhworlddatabase.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.gatheringhallstudios.mhworlddatabase.data.views.Armor;
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasic;

import java.util.List;

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
public abstract class ArmorDao {
    @Query( "SELECT a.*, at.name " +
            "FROM armor a JOIN armor_text at USING (id)" +
            "WHERE at.lang_id = :langId " +
            "   AND a.rarity BETWEEN :minRarity AND :maxRarity " +
            "ORDER BY a.id ASC")
    public abstract LiveData<List<ArmorBasic>> loadList(String langId, int minRarity, int maxRarity);

    public LiveData<List<ArmorBasic>> loadList(String langId) {
        return loadList(langId, 0, 999);
    }

    @Query( "SELECT a.*, at.name " +
            "FROM armor a JOIN armor_text at USING (id) " +
            "WHERE at.lang_id = :langId " +
            "AND a.id = :armorId")
    public abstract LiveData<Armor> loadArmor(String langId, int armorId);
}
