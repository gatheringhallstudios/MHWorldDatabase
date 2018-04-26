package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView;

import java.util.List;

/**
 * A ViewModel for any monster summary fragment
 */
public class MonsterDetailViewModel extends AndroidViewModel {
    private MonsterDao dao;

    private int id;

    boolean initialized = false;
    private LiveData<MonsterView> monster;
    private LiveData<List<MonsterRewardView>> monsterRewards;

    public MonsterDetailViewModel(@NonNull Application application) {
        // todo: perhaps inject the database directly?
        super(application);
        MHWDatabase db = MHWDatabase.getDatabase(application);
        dao = db.monsterDao();
    }

    public void setMonster(int monsterId){
        // Query monster by ID
        this.id = monsterId;
        monster = dao.loadMonster("en", monsterId);
        monsterRewards = dao.loadRewards("en", monsterId);

        initialized = true;
    }

    /**
     * Throws an error if the viewmodel has not been initialized
     */
    private void ensureInitialized() {
        if (!initialized) {
            throw new IllegalStateException("ViewModel not initialized");
        }
    }

    public LiveData<MonsterView> getData() {
        ensureInitialized();
        return monster;
    }

    public LiveData<List<MonsterRewardView>> getRewards() {
        ensureInitialized();
        return monsterRewards;
    }
}
