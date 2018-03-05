package com.gathinghallstudios.mhworlddatabase.data;

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
    public abstract LiveData<List<Monster>> loadAllMonsters(String langId);
}
