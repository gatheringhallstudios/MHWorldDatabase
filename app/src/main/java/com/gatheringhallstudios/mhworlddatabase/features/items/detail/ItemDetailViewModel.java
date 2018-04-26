package com.gatheringhallstudios.mhworlddatabase.features.items.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.ItemDao;
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView;

public class ItemDetailViewModel extends AndroidViewModel {
    private ItemDao dao;

    boolean initialized = false;
    private LiveData<ItemView> item;

    public ItemDetailViewModel(Application app) {
        super(app);

        dao = MHWDatabase.getDatabase(app).itemDao();
    }

    public void setItem(int itemId) {
        item = dao.getItem("en", itemId);
        initialized = true;
    }

    /**
     * Internal helper that throws if the viewmodel wasn't initialized
     */
    private void ensureInitialized() {
        if (!initialized) {
            throw new IllegalStateException("ViewModel not initialized");
        }
    }

    public LiveData<ItemView> getItem() {
        ensureInitialized();
        return item;
    }
}
