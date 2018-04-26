package com.gatheringhallstudios.mhworlddatabase.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHitzoneView;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView;
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
    public abstract LiveData<List<MonsterView>> loadList(String langId);

    @Query( "SELECT m.*, t.name, t.description " +
            "from monster m JOIN monster_text t USING (id) " +
            "WHERE t.lang_id = :langId " +
            "  AND m.size = :size " +
            "ORDER BY t.name ASC")
    public abstract LiveData<List<MonsterView>> loadList(String langId, MonsterSize size);

    @Query( "SELECT m.*, t.name, t.description " +
            "from monster m JOIN monster_text t USING (id) " +
            "WHERE t.lang_id = :langId " +
            "  AND m.id = :id " +
            "LIMIT 1")
    public abstract LiveData<MonsterView> loadMonster(String langId, int id);

    @Query( "SELECT r.monster_id, r.item_id, ct.name condition, it.name item_name, " +
            "   r.rank, r.stack_size, r.percentage " +
            "FROM monster_reward r " +
            "   JOIN monster_reward_condition_text ct " +
            "       ON ct.id = r.condition_id" +
            "       AND ct.lang_id = :langId " +
            "   JOIN item_text it " +
            "       ON it.id = r.item_id" +
            "       AND it.lang_id = :langId " +
            "WHERE r.monster_id = :monsterId")
    public abstract LiveData<List<MonsterRewardView>> loadRewards(String langId, int monsterId);

    @Query( "SELECT h.monster_id, pt.name body_part, h.cut, h.impact, h.shot, " +
            "   h.fire, h.water, h.ice, h.thunder, h.dragon, h.ko " +
            "FROM monster_hitzone h" +
            "   JOIN monster_part_text pt " +
            "       ON pt.id = h.part_id" +
            "       AND pt.lang_id = :langId " +
            "WHERE h.monster_id = :monsterId")
    public abstract LiveData<List<MonsterHitzoneView>> loadHitzones(String langId, int monsterId);

}
