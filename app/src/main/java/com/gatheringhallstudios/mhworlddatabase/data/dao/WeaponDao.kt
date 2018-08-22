package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponBase


@Dao
abstract class WeaponDao {

    //TODO: expand this weapon dao when we actually need the weapon data
//    @Query("SELECT DISTINCT w.weapon_type " +
//            "FROM weapon w  " +
//            "WHERE t.lang_id = :langId ")
//    abstract fun loadWeaponTypes(langId: String): LiveData<WeaponBase>
}
