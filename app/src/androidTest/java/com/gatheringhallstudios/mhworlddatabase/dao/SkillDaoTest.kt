package com.gatheringhallstudios.mhworlddatabase.dao

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao

import org.junit.Assert
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import com.gatheringhallstudios.mhworlddatabase.getResult

@RunWith(AndroidJUnit4::class)
class SkillDaoTest {
    companion object {
        private lateinit var db: MHWDatabase
        private lateinit var dao: SkillDao

        @BeforeClass @JvmStatic
        fun initDatabase() {
            // this is read only, so its ok to use the actual database
            val ctx = InstrumentationRegistry.getTargetContext()
            db = MHWDatabase.getDatabase(ctx)
            dao = db.skillDao()
        }

        @AfterClass @JvmStatic
        fun closeDatabase() {
            db.close()
        }
    }

    @Test
    fun Can_Query_SkillList() {
        val results = dao.loadSkillTrees("en").getResult()
        Assert.assertFalse("expected results", results.isEmpty())
    }

    @Test
    fun Can_Query_Skill() {
        // this is a hardcoded test, but we don't have any other way to test this...
        // this tests if the joining works

        val skillTree = dao.loadSkillTree("en", 15).getResult()
        Assert.assertEquals("name should match", "Attack Boost", skillTree.name)
        Assert.assertEquals("levels should match", 7, skillTree.skills.size)

        // test a skill (any is fine, we just picked one
        val skill = skillTree.skills[2]
        Assert.assertEquals("level should match", 3, skill.level)
        Assert.assertEquals("description should match", "Attack +9", skill.description)
    }
}
