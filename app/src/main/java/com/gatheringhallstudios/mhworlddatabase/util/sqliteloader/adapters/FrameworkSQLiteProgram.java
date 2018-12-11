package com.gatheringhallstudios.mhworlddatabase.util.sqliteloader.adapters;

import androidx.sqlite.db.SupportSQLiteProgram;
import android.database.sqlite.SQLiteProgram;

/**
 * An wrapper around {@link SQLiteProgram} to implement {@link SupportSQLiteProgram} API.
 */
class FrameworkSQLiteProgram implements SupportSQLiteProgram {
    private final SQLiteProgram mDelegate;

    FrameworkSQLiteProgram(SQLiteProgram delegate) {
        mDelegate = delegate;
    }

    @Override
    public void bindNull(int index) {
        mDelegate.bindNull(index);
    }

    @Override
    public void bindLong(int index, long value) {
        mDelegate.bindLong(index, value);
    }

    @Override
    public void bindDouble(int index, double value) {
        mDelegate.bindDouble(index, value);
    }

    @Override
    public void bindString(int index, String value) {
        mDelegate.bindString(index, value);
    }

    @Override
    public void bindBlob(int index, byte[] value) {
        mDelegate.bindBlob(index, value);
    }

    @Override
    public void clearBindings() {
        mDelegate.clearBindings();
    }

    @Override
    public void close() {
        mDelegate.close();
    }
}
