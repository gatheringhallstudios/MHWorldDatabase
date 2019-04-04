package com.gatheringhallstudios.mhworlddatabase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gatheringhallstudios.mhworlddatabase.data.dao.BookmarksDao
import com.gatheringhallstudios.mhworlddatabase.data.dao.UserEquipmentSetDao
import com.gatheringhallstudios.mhworlddatabase.data.entities.BookmarkEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentDecorationEntity
import com.gatheringhallstudios.mhworlddatabase.data.entities.UserEquipmentSetEntity

@Database(entities = [BookmarkEntity::class, UserEquipmentSetEntity::class, UserEquipmentDecorationEntity::class], version = 1)
@TypeConverters(AppConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarksDao
    abstract fun userEquipmentSetDao(): UserEquipmentSetDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "ApplicationDatabase").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}