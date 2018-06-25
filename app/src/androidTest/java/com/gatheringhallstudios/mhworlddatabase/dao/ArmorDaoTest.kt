package com.gatheringhallstudios.mhworlddatabase.dao

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

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
        val results = dao.loadArmorList("en").getResult()
        assertFalse("expected results", results.isEmpty())
    }

    @Test
    fun Can_Filter_ArmorList_Rarity() {
        val results = dao.loadArmorList("en", 3, 3).getResult()
        val allAre3 =  results.all { it.rarity == 3 }
        assertTrue("All armor should be rarity 3", allAre3)
    }

    @Test
    fun Can_Query_Armor() {
        val armorId = 401 // Zorah Hide Alpha
        val result = dao.loadArmor("en", armorId).getResult()

        assertEquals("expect name match", "Zorah Hide Alpha", result.name)
        assertEquals("expected type to match", ArmorType.CHEST, result.armor_type)
    }

    @Test
    fun Can_Query_Armor_Sets_List() {
        val armorSets = dao.loadArmorSets("en", Rank.HIGH).getResult()

        for (set in armorSets) {
            var isAllEmpty = true
            if (set.head_armor != null) isAllEmpty = false
            if (set.chest_armor != null) isAllEmpty = false
            if (set.arms_armor != null) isAllEmpty = false
            if (set.waist_armor != null) isAllEmpty = false
            if (set.legs_armor != null) isAllEmpty = false

            assertFalse("expected non-empty armor", isAllEmpty)
        }

        assertFalse("expect list to be populated", armorSets.isEmpty())
    }
}
