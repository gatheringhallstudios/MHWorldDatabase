package com.gatheringhallstudios.mhworlddatabase.util.sqliteloader.adapters;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates an adapter that converts a SQLiteOpenHelper to a SupportSQLiteOpenHelper.
 * SQLiteOpenHelpers have more configurable creates and upgrades,
 * but a support variant is required for Room. This class bridges that gap.
 * @author Carlos Fernandez
 */
public class SupportSQLiteOpenHelperAdapter implements SupportSQLiteOpenHelper {
    SQLiteOpenHelper innerHelper;

    /**
     * Constructs a new SupportSQliteOpenHelperAdapter to adapt the given SQLiteOpenHelper
     * @param adapted
     */
    public SupportSQLiteOpenHelperAdapter(SQLiteOpenHelper adapted) {
        innerHelper = adapted;
    }

    @Override
    public String getDatabaseName() {
        return innerHelper.getDatabaseName();
    }

    @Override
    public void setWriteAheadLoggingEnabled(boolean enabled) {
        innerHelper.setWriteAheadLoggingEnabled(enabled);
    }

    @Override
    public SupportSQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = innerHelper.getWritableDatabase();
        return new FrameworkSQLiteDatabase(db);
    }

    @Override
    public SupportSQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = innerHelper.getReadableDatabase();
        return new FrameworkSQLiteDatabase(db);
    }

    @Override
    public void close() {
        innerHelper.close();
    }
}
