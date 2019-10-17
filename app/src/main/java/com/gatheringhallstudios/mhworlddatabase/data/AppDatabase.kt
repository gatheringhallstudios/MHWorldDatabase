package com.gatheringhallstudios.mhworlddatabase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.BookmarksDao
import com.gatheringhallstudios.mhworlddatabase.data.dao.UserEquipmentSetDao
import com.gatheringhallstudios.mhworlddatabase.data.entities.BookmarkEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentDecorationEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentSetEntity

@Database(entities = [
    BookmarkEntity::class,
    UserEquipmentEntity::class,
    UserEquipmentSetEntity::class,
    UserEquipmentDecorationEntity::class],
        version = 2)
@TypeConverters(AppConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarksDao
    abstract fun userEquipmentSetDao(): UserEquipmentSetDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "ApplicationDatabase")
                            .addMigrations(MIGRATION_1_2)
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}

/**
 * DATABASE MIGRATIONS
 */

val MIGRATION_1_2 = object: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `user_equipment_sets` 
            (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL);
        """.trimIndent())
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `user_equipment_set_equipment` 
            (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dataId` INTEGER NOT NULL, 
            `dataType` TEXT NOT NULL, `equipmentSetId` INTEGER NOT NULL);
        """.trimIndent())
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `user_equipment_set_decorations` 
            (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `equipmentSetId` 
            INTEGER NOT NULL, `dataId` INTEGER NOT NULL, `dataType` TEXT NOT NULL, 
            `decorationId` INTEGER NOT NULL, `slotNumber` INTEGER NOT NULL);
        """.trimIndent())
    }
}