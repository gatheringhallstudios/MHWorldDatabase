package com.gatheringhallstudios.mhworlddatabase.dao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao;
import com.gatheringhallstudios.mhworlddatabase.data.views.Skill;
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTree;
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeBasic;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static com.gatheringhallstudios.mhworlddatabase.TestUtils.getValue;

@RunWith(AndroidJUnit4.class)
public class SkillDaoTest {
    private static MHWDatabase db;
    private static SkillDao dao;

    @BeforeClass
    public static void initDatabase() {
        // this is read only, so its ok to use the actual database
        Context ctx = InstrumentationRegistry.getTargetContext();
        db = MHWDatabase.getDatabase(ctx);
        dao = db.skillDao();
    }

    @AfterClass
    public static void closeDatabase() {
        db.close();
    }


    @Test
    public void Can_Query_SkillList() throws Exception {
        List<SkillTreeBasic> results = getValue(dao.loadSkillTreeList("en"));
        assertFalse("expected results", results.isEmpty());
    }

    @Test
    public void Can_Query_Skill() throws Exception {
        // this is a hardcoded test, but we don't have any other way to test this...
        // this tests if the joining works

        SkillTree result = getValue(dao.loadSkill("en", 7));
        assertEquals("name should match", "Attack Boost", result.name);
        assertEquals("levels should match", 7, result.skills.size());

        // test a random skill
        Skill attack3 = result.skills.get(2);
        assertEquals("level should match", 3, attack3.level);
        assertEquals("description should match", "Attack +9", attack3.description);
    }
}
