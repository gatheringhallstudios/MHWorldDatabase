package com.gatheringhallstudios.mhworlddatabase.data;

import android.app.Application;
import android.content.Context;

import com.gatheringhallstudios.mhworlddatabase.BuildConfig;
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.BulkLoaderDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.CharmDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.DecorationDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.ItemDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.KinsectDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.LocationDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MetaDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.QuestDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.SearchDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao;
import com.gatheringhallstudios.mhworlddatabase.data.dao.WeaponDao;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorSetBonusEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorSetBonusTextEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorSetTextEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorSkill;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.CharmEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.CharmSkill;
import com.gatheringhallstudios.mhworlddatabase.data.entities.CharmText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.DecorationEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.DecorationText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ItemCombinationEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ItemEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.ItemText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.KinsectEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.KinsectText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.Language;
import com.gatheringhallstudios.mhworlddatabase.data.entities.LocationCampText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.LocationItemEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.LocationText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterBreakEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterBreakText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterHabitatEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterHitzoneEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterHitzoneText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterRewardConditionText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterRewardEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.QuestEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.QuestMonsterEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.QuestRewardEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.QuestText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.RecipeItemEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.SkillEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.SkillTreeEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.SkillTreeText;
import com.gatheringhallstudios.mhworlddatabase.data.entities.WeaponAmmoEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.WeaponEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.WeaponMelodyEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.WeaponMelodyNotesEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.WeaponMelodyTextEntity;
import com.gatheringhallstudios.mhworlddatabase.data.entities.WeaponSkill;
import com.gatheringhallstudios.mhworlddatabase.data.entities.WeaponText;
import com.gatheringhallstudios.mhworlddatabase.util.sqliteloader.SQLiteAssetHelperFactory;

import java.util.List;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * DO NOT CONVERT TO KOTLIN YET.
 * we don't really gain anything from the conversion,
 * and the static methods become more complicated.
 * Created by Carlos on 3/4/2018.
 */
@Database(
        version = BuildConfig.DB_VERSION,
        exportSchema = false,

        entities = {
                // Meta
                Language.class,

                // General (reused)
                RecipeItemEntity.class,

                // Items
                ItemEntity.class, ItemText.class, ItemCombinationEntity.class,

                // Location classes
                LocationText.class, LocationItemEntity.class, LocationCampText.class,

                // monster classes
                MonsterEntity.class, MonsterText.class, MonsterHabitatEntity.class,
                MonsterBreakEntity.class, MonsterBreakText.class,
                MonsterHitzoneEntity.class, MonsterHitzoneText.class,
                MonsterRewardEntity.class, MonsterRewardConditionText.class,

                // Skills
                SkillTreeEntity.class, SkillTreeText.class, SkillEntity.class,

                // Charms
                CharmEntity.class, CharmSkill.class, CharmText.class,

                //Decorations
                DecorationEntity.class, DecorationText.class,

                // Armor classes
                ArmorEntity.class, ArmorText.class, ArmorSkill.class, ArmorSetTextEntity.class,
                ArmorSetBonusEntity.class, ArmorSetBonusTextEntity.class,

                // Weapon Classes
                WeaponEntity.class, WeaponText.class, WeaponAmmoEntity.class,
                WeaponMelodyEntity.class, WeaponMelodyNotesEntity.class, WeaponMelodyTextEntity.class, WeaponSkill.class,

                QuestEntity.class, QuestText.class, QuestMonsterEntity.class, QuestRewardEntity.class,

                // Kinsects
                KinsectEntity.class, KinsectText.class
        })

@TypeConverters({Converters.class})
public abstract class MHWDatabase extends RoomDatabase {
    private static MHWDatabase instance;

    /**
     * Returns a singleton instance of the MHWDatabase object.
     *
     * @param app
     * @return
     */
    public static MHWDatabase getDatabase(Application app) {
        return getDatabase(app.getApplicationContext());
    }

    /**
     * Returns a singleton instance of the MHWDatabase object.
     *
     * @param ctx
     * @return
     */
    public static MHWDatabase getDatabase(Context ctx) {
        // NOTE on implementation
        // We use a custom SQLiteAssetHelperFactor that copies over the database
        // on Room migration attempts. Make sure that the version
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

    public abstract CharmDao charmDao();

    public abstract SearchDao searchDao();

    public abstract DecorationDao decorationDao();

    public abstract WeaponDao weaponDao();

    public abstract QuestDao questDao();

    public abstract KinsectDao kinsectDao();

    public abstract BulkLoaderDao bookmarksSearchDao();

    /** Internal method to recieve meta information queries **/
    protected abstract MetaDao metaDao();

    /**
     * Retrieves all languages supported by the database
     */
    public List<Language> getLanguages() {
        return metaDao().queryLanguages();
    }
}
