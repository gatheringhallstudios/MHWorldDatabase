package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.views.Hitzone;

import java.util.ArrayList;
import java.util.List;

/**
 * A ViewModel for any monster reward fragment
 */
public class MonsterDamageViewModel extends AndroidViewModel {

    private MonsterDao dao;

    private LiveData<List<Hitzone>> hitzones;
    // private LiveData<List<Status>> status; // Placeholder for Monster Status Procs

    public MonsterDamageViewModel(@NonNull Application application) {
        super(application);
        MHWDatabase db = MHWDatabase.getDatabase(application);
        dao = db.monsterDao();
    }

    public void setMonsterId(int monsterId) {
        // TODO Load rewards from actual data
        loadMockData();
    }

    public LiveData<List<Hitzone>> getHitzoneData() {
        if (hitzones == null) {
            throw new IllegalStateException("ViewModel not initialized");
        }
        return hitzones;
    }

    @VisibleForTesting
    public void loadMockData(){
        // Populate with mock hitzones
        List<Hitzone> hitzoneList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Hitzone hitzone = new Hitzone();

            hitzone.id = i;
            hitzone.bodyPart = "Body Part " + i;
            hitzone.cut = mockDamage();
            hitzone.impact = mockDamage();
            hitzone.shot = mockDamage();
            hitzone.ko = mockDamage();
            hitzone.fire = mockDamage();
            hitzone.water = mockDamage();
            hitzone.ice = mockDamage();
            hitzone.thunder = mockDamage();
            hitzone.dragon = mockDamage();

            hitzoneList.add(hitzone);
        }

        MutableLiveData<List<Hitzone>> hitzoneData = new MutableLiveData<>();
        hitzoneData.setValue(hitzoneList);

        hitzones = hitzoneData;
    }

    public int mockDamage() {
        return (int) (Math.random() * 60) + 1;
    }
}
