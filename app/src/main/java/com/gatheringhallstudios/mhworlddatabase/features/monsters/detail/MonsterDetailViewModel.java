package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHabitatView;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHitzoneView;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView;

import java.util.ArrayList;
import java.util.List;

/**
 * A ViewModel for any monster summary fragment
 */
public class MonsterDetailViewModel extends AndroidViewModel {
    private MonsterDao dao;

    private int id;

    boolean initialized = false;
    private LiveData<MonsterView> monster;
    private LiveData<List<MonsterHabitatView>> habitats;
    private LiveData<List<MonsterRewardView>> monsterRewards;
    private LiveData<List<MonsterHitzoneView>> hitzones;

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
        habitats = dao.loadHabitats("en", monsterId);
        monsterRewards = dao.loadRewards("en", monsterId);
        hitzones = dao.loadHitzones("en", monsterId);

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

    public LiveData<List<MonsterHabitatView>> getHabitats() {
        ensureInitialized();
        return habitats;
    }

    public LiveData<List<MonsterRewardView>> getRewards() {
        ensureInitialized();
        return monsterRewards;
    }

    public LiveData<List<MonsterHitzoneView>> getHitzones() {
        ensureInitialized();
        return hitzones;
    }

    // todo: I took the following from the previous version, but is this ok?

    @VisibleForTesting
    public void loadMockData(){
        // Populate with mock hitzones
        List<MonsterHitzoneView> hitzoneList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            MonsterHitzoneView hitzone = new MonsterHitzoneView();

            hitzone.monster_id = id;
            hitzone.body_part = "Body Part " + i;
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

        MutableLiveData<List<MonsterHitzoneView>> hitzoneData = new MutableLiveData<>();
        hitzoneData.setValue(hitzoneList);

        hitzones = hitzoneData;
    }

    public int mockDamage() {
        return (int) (Math.random() * 60) + 1;
    }
}
