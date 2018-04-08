package com.gatheringhallstudios.mhworlddatabase.features.armor;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasic;
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao;

import java.util.List;

/**
 * Created by Carlos on 3/22/2018.
 */

public class ArmorViewModel extends AndroidViewModel {
    private ArmorDao dao;

    public ArmorViewModel(@NonNull Application application) {
        super(application);

        dao = MHWDatabase.getDatabase(application).armorDao();
    }

    public LiveData<List<ArmorBasic>> getArmorList() {
        return dao.loadList("en");
    }
}
