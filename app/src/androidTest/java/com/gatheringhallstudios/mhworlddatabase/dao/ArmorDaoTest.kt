package com.gatheringhallstudios.mhworlddatabase.dao

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import com.gatheringhallstudios.mhworlddatabase.getResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class ArmorDaoTest {
    companion object {
        private lateinit var db: MHWDatabase
        private lateinit var dao: ArmorDao

        @BeforeClass @JvmStatic
        fun initDatabase() {
            // this is read only, so its ok to use the actual database
            val ctx = InstrumentationRegistry.getTargetContext()
            db = MHWDatabase.getDatabase(ctx)
            dao = db.armorDao()
        }

        @AfterClass @JvmStatic
        fun closeDatabase() {
            db.close()
        }
    }
    
    
    @Test
    fun Can_Query_ArmorList() {
        val results = dao.loadArmorList("en", Rank.HIGH).getResult()
        assertFalse("expected results", results.isEmpty())
    }

    @Test
    fun Can_Query_Armor() {
        val armorId = 401 // Zorah Hide Alpha
        val result = dao.loadArmor("en", armorId).getResult()

        assertEquals("expect name match", "Zorah Hide α", result.name)
        assertEquals("expected type to match", ArmorType.CHEST, result.armor_type)
    }

    @Test
    fun Can_Query_Armor_Sets_List() {
        val armorSets = dao.loadArmorSets("en", Rank.HIGH).getResult()

        for (set in armorSets) {
            val isAllEmpty = set.armor.isEmpty()
            assertFalse("expected non-empty armor", isAllEmpty)
        }

        assertFalse("expect list to be populated", armorSets.isEmpty())
    }
}
