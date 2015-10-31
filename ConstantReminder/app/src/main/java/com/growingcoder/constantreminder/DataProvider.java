package com.growingcoder.constantreminder;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.growingcoder.constantreminder.data.gen.DaoMaster;
import com.growingcoder.constantreminder.data.gen.ReminderDao;

/**
 * @author Pierce
 * @since 10/31/2015.
 */
public class DataProvider extends ContentProvider {

    private DaoMaster.DevOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new DaoMaster.DevOpenHelper(App.sAppContext, App.DB_NAME, null);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor c = db.query(ReminderDao.TABLENAME, projection, selection, selectionArgs, null, null, sortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(ReminderDao.TABLENAME, null, values);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.delete(ReminderDao.TABLENAME, selection, selectionArgs);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.update(ReminderDao.TABLENAME, values, selection, selectionArgs);

        return count;
    }
}
