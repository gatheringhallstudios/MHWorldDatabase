package com.gatheringhallstudios.mhworlddatabase

import androidx.test.platform.app.InstrumentationRegistry
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase

fun initMHWDatabase(): MHWDatabase {
    val ctx = InstrumentationRegistry.getInstrumentation().targetContext
    val db = MHWDatabase.getDatabase(ctx)
    return db
}