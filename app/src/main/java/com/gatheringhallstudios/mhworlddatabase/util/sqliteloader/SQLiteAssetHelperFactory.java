package com.gatheringhallstudios.mhworlddatabase.util.sqliteloader;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gatheringhallstudios.mhworlddatabase.util.sqliteloader.adapters.SupportSQLiteOpenHelperAdapter;
import com.gatheringhallstudios.mhworlddatabase.util.sqliteloader.sqliteasset.SQLiteAssetHelper;

/**
 * A factory that returns the SQLiteAssetHelper adapted as a SupportSQLiteOpenHelper.
 * This factory can be supplied to Room's database builder through openHelperFactory.
 */
public class SQLiteAssetHelperFactory implements SupportSQLiteOpenHelper.Factory {
    private boolean forceUpgrade;

    /**
     * Constructs a new SQLiteAssetHelperFactory.
     * @param forceUpgrade whether the internal SQLiteAssetHelper should always reload the database.
     */
    public SQLiteAssetHelperFactory(boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    // todo: add a version that specifies the data source

    @Override
    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        SQLiteAssetHelper helper = new SQLiteAssetHelper(
                configuration.context,
                configuration.name,
                null,
                configuration.callback.version);

        if (forceUpgrade) {
            helper.setForcedUpgrade();
        }

        // return an adapter for the helper we just created
        return new SupportSQLiteOpenHelperAdapter(helper);
    }
}
