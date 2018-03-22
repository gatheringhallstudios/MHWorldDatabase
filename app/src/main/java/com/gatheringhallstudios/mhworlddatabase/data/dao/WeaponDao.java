package com.gatheringhallstudios.mhworlddatabase.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.gatheringhallstudios.mhworlddatabase.data.Weapon;
import com.gatheringhallstudios.mhworlddatabase.data.WeaponBasic;
import com.gatheringhallstudios.mhworlddatabase.data.WeaponType;

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
