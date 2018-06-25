package com.gatheringhallstudios.mhworlddatabase.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.gatheringhallstudios.mhworlddatabase.data.views.WeaponBasic;
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType;

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
public abstract class WeaponDao {

    @Query( "SELECT w.* " +
            "FROM weapon w JOIN weapon_text t USING (id) " +
            "WHERE t.lang_id = :langId " +
            "  AND w.weapon_type = :type")
    public abstract LiveData<WeaponBasic> loadByType(String langId, WeaponType type);
}
