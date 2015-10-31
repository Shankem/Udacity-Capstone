package com.growingcoder.constantreminder;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.growingcoder.constantreminder.data.gen.DaoMaster;
import com.growingcoder.constantreminder.data.gen.DaoSession;

/**
 * @author Pierce
 * @since 10/11/2015.
 */
public class App extends Application {
    public static final String DB_NAME = "reminders.db";

    public static DaoSession mDaoSession;
    public static App sAppContext;

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppContext = this;
        startDB();
        App.sAppContext.getAnalyticsTracker().enableAutoActivityTracking(true);
    }

    private void startDB() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    synchronized public Tracker getAnalyticsTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.ga_tracker);
        }
        return mTracker;
    }
}
