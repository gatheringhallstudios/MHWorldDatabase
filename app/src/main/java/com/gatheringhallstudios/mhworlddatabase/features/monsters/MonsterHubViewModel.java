package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.Monster;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.MonsterSize;

import java.util.List;

/**
 * A viewmodel for any monster fragment, used to contain data between fragment recreations.
 * Created by Carlos on 3/4/2018.
 */
public class MonsterHubViewModel extends AndroidViewModel {
    private MonsterDao dao;

    LiveData<List<Monster>> allMonsters;
    LiveData<List<Monster>> largeMonsters;
    LiveData<List<Monster>> smallMonsters;

    public MonsterHubViewModel(@NonNull Application application) {
        super(application);
        MHWDatabase db = MHWDatabase.getDatabase(application);
        dao = db.monsterDao();
    }

    public LiveData<List<Monster>> getMonsters() {
        if (allMonsters != null) {
            return allMonsters;
        }

        allMonsters = dao.loadList("en");
        return allMonsters;
    }

    public LiveData<List<Monster>> getLargeMonsters() {
        if (largeMonsters != null) {
            return largeMonsters;
        }

        largeMonsters = dao.loadList("en", MonsterSize.LARGE);
        return largeMonsters;
    }

    public LiveData<List<Monster>> getSmallMonsters() {
        if (smallMonsters != null) {
            return smallMonsters;
        }

        smallMonsters = dao.loadList("en", MonsterSize.SMALL);
        return smallMonsters;
    }
}
