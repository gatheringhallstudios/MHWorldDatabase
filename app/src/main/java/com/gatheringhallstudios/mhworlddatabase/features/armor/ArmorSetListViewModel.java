package com.gatheringhallstudios.mhworlddatabase.features.armor;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao;
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank;
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView;

import java.util.List;

/**
 * Created by Carlos on 3/22/2018.
 */

public class ArmorSetListViewModel extends AndroidViewModel {
    private ArmorDao dao;

    public ArmorSetListViewModel(@NonNull Application application) {
        super(application);

        dao = MHWDatabase.getDatabase(application).armorDao();
    }

    public LiveData<List<ArmorSetView>> getArmorSetList(Rank rank) {
        return dao.loadArmorSets("en", rank);
    }
}
