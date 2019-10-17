package com.gatheringhallstudios.mhworlddatabase.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.LocationDao
import com.gatheringhallstudios.mhworlddatabase.getResult
import com.gatheringhallstudios.mhworlddatabase.initMHWDatabase
import org.junit.Assert
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationDaoTest {
    companion object {
        private lateinit var db: MHWDatabase
        private lateinit var dao: LocationDao

        @BeforeClass
        @JvmStatic
        fun initDatabase() {
            // this is read only, so its ok to use the actual database
            db = initMHWDatabase()
            dao = db.locationDao()
        }

        @AfterClass
        @JvmStatic
        fun closeDatabase() {
            db.close()
        }
    }

    @Test
    fun Can_Query_Locations() {
        val results = dao.loadLocations("en").getResult()
        Assert.assertFalse("locations should not be empty", results.isEmpty())
    }

    @Test
    fun Can_Query_Location() {
        val allResults = dao.loadLocations("en").getResult()
        val firstResult = allResults.first()

        val queried = dao.loadLocation("en", firstResult.id).getResult()
        Assert.assertEquals("id should be equal", firstResult.id, queried.id)
    }
}