package com.gatheringhallstudios.mhworlddatabase.data;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.location.Location;

// laziness, since we need to manually add all entities which is quite cumbersome
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.ItemDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.LocationDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao;
import com.gatheringhallstudios.mhworlddatabase.data.entities.*;
import com.gatheringhallstudios.mhworlddatabase.util.sqliteloader.SQLiteAssetHelperFactory;

/**
 * DO NOT CONVERT TO KOTLIN YET.
 * we don't really gain anything from the conversion,
 * and the static methods become more complicated.
 * Created by Carlos on 3/4/2018.
 */
@Database(
    entities={
            // Items
            ItemEntity.class, ItemText.class,

            // Location classes
            LocationText.class, LocationItemEntity.class,
    
            // monster classes
            MonsterEntity.class, MonsterText.class, MonsterHabitatEntity.class,
            MonsterBreakEntity.class, MonsterHitzoneEntity.class, MonsterHitzoneText.class,
            MonsterRewardEntity.class, MonsterRewardConditionText.class,
    
            // Skills
            SkillTreeEntity.class, SkillTreeText.class, SkillEntity.class,
    
            // Armor classes
            ArmorEntity.class, ArmorText.class, ArmorSkill.class

    }, version = 4)
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
                    .openHelperFactory(new SQLiteAssetHelperFactory(true))
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ItemDao itemDao();

    public abstract LocationDao locationDao();

    public abstract MonsterDao monsterDao();

    public abstract SkillDao skillDao();

    public abstract ArmorDao armorDao();
}
