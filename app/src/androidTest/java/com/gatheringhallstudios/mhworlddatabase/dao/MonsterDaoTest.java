package com.gatheringhallstudios.mhworlddatabase.dao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.gatheringhallstudios.mhworlddatabase.TestUtils.getValue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class MonsterDaoTest {
    private static MHWDatabase db;
    private static MonsterDao dao;

    @BeforeClass
    public static void initDatabase() {
        // this is read only, so its ok to use the actual database
        Context ctx = InstrumentationRegistry.getTargetContext();
        db = MHWDatabase.getDatabase(ctx);
        dao = db.monsterDao();
    }

    @AfterClass
    public static void closeDatabase() {
        db.close();
    }

    @Test
    public void Can_Query_MonsterList() throws Exception {
        List<MonsterView> results = getValue(dao.loadList("en"));
        assertFalse("expected results", results.isEmpty());
    }
}
