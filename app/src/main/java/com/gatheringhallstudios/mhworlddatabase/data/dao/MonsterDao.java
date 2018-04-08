package com.gatheringhallstudios.mhworlddatabase.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.gatheringhallstudios.mhworlddatabase.data.views.Monster;
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize;

import java.util.List;

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
public abstract class MonsterDao {
    @Query( "SELECT m.*, t.name, t.description " +
            "from monster m JOIN monster_text t USING (id) " +
            "WHERE t.lang_id = :langId " +
            "ORDER BY t.name ASC")
    public abstract LiveData<List<Monster>> loadList(String langId);

    @Query( "SELECT m.*, t.name, t.description " +
            "from monster m JOIN monster_text t USING (id) " +
            "WHERE t.lang_id = :langId " +
            "  AND m.size = :size " +
            "ORDER BY t.name ASC")
    public abstract LiveData<List<Monster>> loadList(String langId, MonsterSize size);
}
