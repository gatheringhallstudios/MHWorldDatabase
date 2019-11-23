package com.gatheringhallstudios.mhworlddatabase.dao

import androidx.test.ext.junit.runners.AndroidJUnit4

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.getResult
import com.gatheringhallstudios.mhworlddatabase.initMHWDatabase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull

@RunWith(AndroidJUnit4::class)
class MonsterDaoTest {

    companion object {
        private lateinit var db: MHWDatabase
        private lateinit var dao: MonsterDao

        @BeforeClass @JvmStatic
        fun initDatabase() {
            // this is read only, so its ok to use the actual database
            db = initMHWDatabase()
            dao = db.monsterDao()
        }

        @AfterClass @JvmStatic
        fun closeDatabase() {
            db.close()
        }
    }

    @Test
    fun Can_Query_MonsterList() {
        val results = dao.loadMonsters("en").getResult()
        assertFalse("expected results", results.isEmpty())
    }

    @Test
    fun Can_Query_MonsterBreaks() {
        val largeMonsters = dao.loadMonsters("en", MonsterSize.LARGE).getResult()
        val monster = largeMonsters.find { it.name == "Rathalos" }
        assertNotNull("Expected Rathalos to exist", monster)

        val breaks = dao.loadBreaks("en", monster!!.id).getResult()
        assertFalse("Expected results", breaks.isEmpty())
    }


}
