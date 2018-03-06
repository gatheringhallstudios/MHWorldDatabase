package com.gatheringhallstudios.mhworlddatabase.data;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.gatheringhallstudios.mhworlddatabase.data.raw.MonsterRaw;
import com.gatheringhallstudios.mhworlddatabase.data.raw.MonsterText;
import com.huma.room_for_asset.RoomAsset;

/**
 * Created by Carlos on 3/4/2018.
 */
@Database(entities={MonsterRaw.class, MonsterText.class}, version=2)
public abstract class MHWDatabase extends RoomDatabase {
    private static MHWDatabase instance;

    /**
     * Returns a singleton instance of the MHWDatabase object.
     * @param app
     * @return
     */
    private static MHWDatabase getDatabase(Application app) {
        return getDatabase(app.getApplicationContext());
    }

    /**
     * Returns a singleton instance of the MHWDatabase object.
     * @param ctx
     * @return
     */
    public static MHWDatabase getDatabase(Context ctx) {
        // NOTE on implementation
        // We use the RoomAsset library because Android cannot query directly from APK read-only data.
        // Therefore, RoomAsset performs a migration from the assets to the user local files.
        // A change in the database will require you to delete local data in the emulator if there is no version bump.
        // https://stackoverflow.com/questions/44263891/how-to-use-room-persistence-library-with-pre-populated-database
        if (instance == null) {
            instance = RoomAsset.databaseBuilder(ctx, MHWDatabase.class, "mhw.db").build();
        }
        return instance;
    }

    public abstract MHWDao mhwDao();
}
