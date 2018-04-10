package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.views.Monster;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize;

import java.util.List;

/**
 * A viewmodel for any monster list fragment
 * Created by Carlos on 3/4/2018.
 */
public class MonsterListViewModel extends AndroidViewModel {
    private MonsterDao dao;

    public enum Tab {
        LARGE,
        SMALL
    }

    Tab currentTab;
    LiveData<List<Monster>> monsters;

    public MonsterListViewModel(@NonNull Application application) {
        // todo: perhaps inject the database directly?
        super(application);
        MHWDatabase db = MHWDatabase.getDatabase(application);
        dao = db.monsterDao();
    }

    public void setTab(Tab tab) {
        if (monsters != null && currentTab == tab) {
            return;
        }

        switch (tab) {
            case LARGE:
                monsters = dao.loadList("en", MonsterSize.LARGE);
                break;
            case SMALL:
                monsters = dao.loadList("en", MonsterSize.SMALL);
                break;
            default:
                monsters = dao.loadList("en");
        }

        currentTab = tab;
    }

    public LiveData<List<Monster>> getData() {
        if (monsters == null) {
            throw new IllegalStateException("ViewModel not initialized");
        }
        return monsters;
    }
}
