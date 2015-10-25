package com.growingcoder.constantreminder.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.widget.RemoteViews;

import com.growingcoder.constantreminder.App;
import com.growingcoder.constantreminder.R;
import com.growingcoder.constantreminder.data.gen.Reminder;
import com.growingcoder.constantreminder.data.gen.ReminderDao;

import java.util.Date;
import java.util.List;

/**
 * @author Pierce
 * @since 10/25/2015.
 */
public class WidgetService extends IntentService {

    public WidgetService() {
        super(WidgetService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        if(allWidgetIds == null || allWidgetIds.length == 0){
            return;
        }


        List<Reminder> reminders = App.mDaoSession.getReminderDao().queryBuilder().orderAsc(ReminderDao.Properties.DateTime).list();

        Reminder r = null;
        if (reminders.size() > 0) {
            r = reminders.get(0);
        }

        for (int widgetId : allWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_reminder);

            setupViews(remoteViews, r);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void setupViews(final RemoteViews remoteViews, Reminder r) {

        if (r != null) {
            remoteViews.setTextViewText(R.id.card_reminder_textview_title, r.getName());
            remoteViews.setTextViewText(R.id.card_reminder_textview_time, DateUtils.getRelativeDateTimeString(
                    App.sAppContext,
                    r.getDateTime().longValue(),
                    DateUtils.DAY_IN_MILLIS,
                    0l,
                    0));

            if (r.getDateTime() > new Date().getTime()) {
                remoteViews.setTextColor(R.id.card_reminder_textview_time,
                        App.sAppContext.getResources().getColor(R.color.reminder_current));
            } else {
                remoteViews.setTextColor(R.id.card_reminder_textview_time,
                        App.sAppContext.getResources().getColor(R.color.reminder_past));
            }
        }
    }


    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }
}
