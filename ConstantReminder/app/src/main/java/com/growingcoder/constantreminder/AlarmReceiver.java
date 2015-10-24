package com.growingcoder.constantreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.growingcoder.constantreminder.data.gen.Reminder;

/**
 * Listens for the alarm to go off so a notification can be displayed to the user.
 *
 * @author Pierce
 * @since 10/24/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION = "REMINDER_ACTION";
    public static final String EXTRA_REMINDER = "EXTRA_REMINDER";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            long id = intent.getLongExtra(EXTRA_REMINDER, 0);
            if (id > 0) {
                Reminder reminder = App.mDaoSession.getReminderDao().load(id);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                builder.setContentTitle(context.getString(R.string.notification_title));
                builder.setContentText(reminder.getName());
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setAutoCancel(false);
                builder.setOngoing(true);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);

                Intent activityIntent = new Intent(context, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

                builder.setContentIntent(contentIntent);

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(reminder.getId().intValue(), builder.build());
            }
        }
    }
}
