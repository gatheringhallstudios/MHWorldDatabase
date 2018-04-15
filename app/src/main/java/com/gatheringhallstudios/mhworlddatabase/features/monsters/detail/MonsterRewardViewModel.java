package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.views.Reward;

import java.util.ArrayList;
import java.util.List;

/**
 * A ViewModel for any monster reward fragment
 */
public class MonsterRewardViewModel extends AndroidViewModel {

    private MonsterDao dao;

    private LiveData<List<Reward>> rewards;

    public MonsterRewardViewModel(@NonNull Application application) {
        // todo: perhaps inject the database directly?
        super(application);
        MHWDatabase db = MHWDatabase.getDatabase(application);
        dao = db.monsterDao();
    }

    public void setMonsterId(int monsterId) {
        // TODO Load rewards from actual data
        loadMockData();
    }

    public LiveData<List<Reward>> getData() {
        if (rewards == null) {
            throw new IllegalStateException("ViewModel not initialized");
        }
        return rewards;
    }

    @VisibleForTesting
    public void loadMockData(){
        // Populate with mock rewards
        List<Reward> rewardList = new ArrayList<Reward>();

        String rank = "lr";
        for (int i = 0; i < 3; i++) { // Conditions
            String condition = "Condition " + i;
            for (int j = 0; j < 5; j++) {
                Reward reward = new Reward();
                reward.id = (i * 10) + j;
                reward.condition = condition;
                reward.name = "Reward " + j;
                reward.stackSize = (int) (Math.random() * 5) + 1;
                reward.percentage = (int) (Math.random() * 100) + 1;
                reward.rank = rank;
                rewardList.add(reward);
            }
        }

        rank = "hr";
        for (int i = 0; i < 3; i++) { // Conditions
            String condition = "Condition " + i;
            for (int j = 0; j < 5; j++) {
                Reward reward = new Reward();
                reward.id = (i * 10) + j;
                reward.condition = condition;
                reward.name = "Reward " + j;
                reward.stackSize = (int) (Math.random() * 5) + 1;
                reward.percentage = (int) (Math.random() * 100) + 1;
                reward.rank = rank;
                rewardList.add(reward);
            }
        }

        MutableLiveData<List<Reward>> rewardData = new MutableLiveData<>();
        rewardData.setValue(rewardList);

        rewards = rewardData;
    }
}
