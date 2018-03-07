package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.Monster;

import java.util.List;

/**
 * A viewmodel for any monster fragment, used to contain data between fragment recreations.
 * Created by Carlos on 3/4/2018.
 */
public class MonsterViewModel extends AndroidViewModel {
    private MHWDatabase db;
    private LiveData<List<Monster>> monsters;

    public MonsterViewModel(@NonNull Application application) {
        super(application);
        db = MHWDatabase.getDatabase(application);
    }

    public LiveData<List<Monster>> getMonsters() {
        if (monsters != null) {
            return monsters;
        }

        monsters = db.mhwDao().loadMonsterList("en");
        return monsters;
    }
}
