package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.views.Monster;

/**
 * A ViewModel for any monster summary fragment
 */
public class MonsterSummaryViewModel extends AndroidViewModel {
    private MonsterDao dao;

    private LiveData<Monster> monster;

    public MonsterSummaryViewModel(@NonNull Application application) {
        // todo: perhaps inject the database directly?
        super(application);
        MHWDatabase db = MHWDatabase.getDatabase(application);
        dao = db.monsterDao();
    }

    public void setMonster(int monsterId){
        // Query monster by ID
        monster = dao.loadMonster("en", monsterId);
    }

    public LiveData<Monster> getData() {
        if (monster == null) {
            throw new IllegalStateException("ViewModel not initialized");
        }
        return monster;
    }
}
