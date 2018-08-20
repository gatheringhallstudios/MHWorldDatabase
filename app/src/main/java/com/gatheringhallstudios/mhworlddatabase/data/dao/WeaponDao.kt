package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponBase

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
abstract class WeaponDao {

//    @Query("SELECT DISTINCT w.weapon_type " +
//            "FROM weapon w  " +
//            "WHERE t.lang_id = :langId ")
//    abstract fun loadWeaponTypes(langId: String): LiveData<WeaponBase>
}
