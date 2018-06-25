package com.gatheringhallstudios.mhworlddatabase.dao

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import com.gatheringhallstudios.mhworlddatabase.TestUtils.getValue
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.getResult
import org.junit.Assert.assertFalse

@RunWith(AndroidJUnit4::class)
class MonsterDaoTest {

    companion object {
        private lateinit var db: MHWDatabase
        private lateinit var dao: MonsterDao

        @BeforeClass @JvmStatic
        fun initDatabase() {
            // this is read only, so its ok to use the actual database
            val ctx = InstrumentationRegistry.getTargetContext()
            db = MHWDatabase.getDatabase(ctx)
            dao = db.monsterDao()
        }

        @AfterClass @JvmStatic
        fun closeDatabase() {
            db.close()
        }
    }

    @Test
    fun Can_Query_MonsterList() {
        val results = dao.loadList("en").getResult()
        assertFalse("expected results", results.isEmpty())
    }

    @Test
    fun Can_Query_MonsterBreaks() {
        val largeMonsters = dao.loadList("en", MonsterSize.LARGE).getResult()
        val firstMonster = largeMonsters.first()

        val breaks = dao.loadBreaks("en", firstMonster.id).getResult()
        assertFalse("Expected results", breaks.isEmpty())
    }


}
