package com.growingcoder.constantreminder;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.growingcoder.constantreminder.data.gen.DaoMaster;
import com.growingcoder.constantreminder.data.gen.DaoSession;

/**
 * @author Pierce
 * @since 10/11/2015.
 */
public class App extends Application {
    private static final String DB_NAME = "reminders.db";

    public static DaoSession mDaoSession;
    public static App sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppContext = this;
        startDB();
    }

    private void startDB() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }
}
