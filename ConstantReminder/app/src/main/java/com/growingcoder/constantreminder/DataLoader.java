package com.growingcoder.constantreminder;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.growingcoder.constantreminder.data.gen.Reminder;

import java.util.List;

/**
 * @author Pierce
 * @since 10/31/2015.
 */
public class DataLoader extends AsyncTaskLoader<List<Reminder>> {
    public DataLoader(Context context) {
        super(context);
    }

    @Override
    public List<Reminder> loadInBackground() {
        return App.mDaoSession.getReminderDao().loadAll();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
