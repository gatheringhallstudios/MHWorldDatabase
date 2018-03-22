package com.gatheringhallstudios.mhworlddatabase.data;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

// laziness, since we need to manually add all entities which is quite cumbersome
import com.fstyle.library.helper.AssetSQLiteOpenHelperFactory;
import com.gatheringhallstudios.mhworlddatabase.data.raw.*;

/**
 * Created by Carlos on 3/4/2018.
 */
@Database(
    entities={
        MonsterRaw.class, MonsterText.class,
        SkillTreeRaw.class, SkillTreeText.class, SkillRaw.class,
        ArmorRaw.class, ArmorText.class, ArmorSkill.class
    }, version=2)
@TypeConverters({ Converters.class })
public abstract class MHWDatabase extends RoomDatabase {
    private static MHWDatabase instance;

    /**
     * Returns a singleton instance of the MHWDatabase object.
     * @param app
     * @return
     */
    public static MHWDatabase getDatabase(Application app) {
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
            instance = Room.databaseBuilder(ctx, MHWDatabase.class, "mhw.db")
                    .openHelperFactory(new AssetSQLiteOpenHelperFactory())
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract MHWDao mhwDao();
}
