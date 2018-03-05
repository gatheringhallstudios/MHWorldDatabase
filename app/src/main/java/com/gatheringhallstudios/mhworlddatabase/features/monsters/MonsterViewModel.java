package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.Monster;

import java.util.List;

/**
 * A viewmodel for any monster fragment, used to contain data between fragment recreations.
 * Created by Carlos on 3/4/2018.
 */
public class MonsterViewModel extends ViewModel {
    // todo: inject the database object using dagger 2?

    private LiveData<List<Monster>> monsters;

    public LiveData<List<Monster>> getMonsters(Context ctx) {
        if (monsters != null) {
            return monsters;
        }

        MHWDatabase db = MHWDatabase.getDatabase(ctx);
        monsters = db.mhwDao().loadAllMonsters("en");
        return monsters;
    }
}
