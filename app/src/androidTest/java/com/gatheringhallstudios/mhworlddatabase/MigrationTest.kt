package com.gatheringhallstudios.mhworlddatabase

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import org.junit.Test
import org.junit.runner.RunWith
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase_MIGRATIONS
import org.junit.Rule

/**
 * Class used to test if database migrations are functional
 */
@RunWith(AndroidJUnit4::class)
class MigrationTest {
    val helper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory())

    @Test
    fun migrateAll() {
        val dbName = "migration_test"

        // create initial DB version
        val db = helper.createDatabase(dbName, 1)
        db.close()

        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val appDB = Room.databaseBuilder(
                ctx, AppDatabase::class.java, dbName
        ).addMigrations(*AppDatabase_MIGRATIONS).build()

        appDB.openHelper.writableDatabase
        appDB.close()
    }
}