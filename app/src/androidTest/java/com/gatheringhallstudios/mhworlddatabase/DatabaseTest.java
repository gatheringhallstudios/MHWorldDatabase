package com.gatheringhallstudios.mhworlddatabase;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.gatheringhallstudios.mhworlddatabase.data.views.Armor;
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType;
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.views.Monster;
import com.gatheringhallstudios.mhworlddatabase.data.views.Skill;
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTree;
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeBasic;
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasic;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * Created by Carlos on 3/6/2018.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static MHWDatabase db;

    @BeforeClass
    public static void initDatabase() {
        // this is read only, so its ok to use the actual database
        Context ctx = InstrumentationRegistry.getTargetContext();
        db = MHWDatabase.getDatabase(ctx);
    }

    @AfterClass
    public static void closeDatabase() {
        db.close();
    }

    public static <T> T getValue(LiveData<T> data) throws InterruptedException {
        AtomicReference<T> value = new AtomicReference<T>(); // lambda can only set to effectively final
        CountDownLatch latch = new CountDownLatch(1);
        data.observeForever((result) -> {
            value.set(result);

        });
        latch.await(2, TimeUnit.SECONDS);
        return value.get();
    }

    @Test
    public void Can_Query_MonsterList() throws Exception {
        List<Monster> results = getValue(db.monsterDao().loadList("en"));
        assertFalse("expected results", results.isEmpty());
    }

    @Test
    public void Can_Query_SkillList() throws Exception {
        List<SkillTreeBasic> results = getValue(db.skillDao().loadSkillTreeList("en"));
        assertFalse("expected results", results.isEmpty());
    }

    @Test
    public void Can_Query_Skill() throws Exception {
        // this is a hardcoded test, but we don't have any other way to test this...
        // this tests if the joining works

        SkillTree result = getValue(db.skillDao().loadSkill("en", 5));
        assertEquals("name should match", "Attack Boost", result.name);
        assertEquals("levels should match", 7, result.skills.size());

        // test a random skill
        Skill attack3 = result.skills.get(2);
        assertEquals("level should match", 3, attack3.level);
        assertEquals("description should match", "Attack +9", attack3.description);
    }

    @Test
    public void Can_Query_ArmorList() throws Exception {
        List<ArmorBasic> results = getValue(db.armorDao().loadList("en"));
        assertFalse("expected results", results.isEmpty());
    }

    @Test
    public void Can_Filter_ArmorList_Rarity() throws Exception {
        List<ArmorBasic> results = getValue(db.armorDao().loadList("en", 3, 3));
        boolean allAre3 = results.stream().allMatch((a) -> a.rarity == 3);
        assertTrue("All armor should be rarity 3", allAre3);
    }

    @Test
    public void Can_Query_Armor() throws Exception {
        int armorId = 489; // Zorah Hide Alpha
        Armor result = getValue(db.armorDao().loadArmor("en", armorId));

        assertEquals("expect name match", "Zorah Hide Alpha", result.name);
        assertEquals("expected type to match", ArmorType.CHEST, result.armor_type);
    }
}
