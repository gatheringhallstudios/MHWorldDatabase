package com.gatheringhallstudios.mhworlddatabase.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTree;
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeBasic;

import java.util.List;

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
public abstract class SkillDao {
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
}
