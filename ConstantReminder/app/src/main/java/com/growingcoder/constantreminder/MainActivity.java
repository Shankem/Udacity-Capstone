package com.growingcoder.constantreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.growingcoder.constantreminder.data.gen.Reminder;
import com.growingcoder.constantreminder.data.gen.ReminderDao;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Reminder>> {

    private ReminderAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.activity_main_fab_add).setOnClickListener(new AddReminderClickListener());

        setupViews();

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    private void setupViews() {
        mEmptyView = findViewById(R.id.activity_main_textview_empty);

        mAdapter = new ReminderAdapter();
        mAdapter.setItemClickListener(new ReminderClickListener());

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerview_reminders);
        mRecyclerView.setHasFixedSize(true);

        int margin = (int) getResources().getDimension(R.dimen.card_vertical_space);
        mRecyclerView.addItemDecoration(new VerticalSpaceDecoration(margin));

        mLayoutManager = new LinearLayoutManager(App.sAppContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public Loader<List<Reminder>> onCreateLoader(int id, Bundle args) {
        return new DataLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Reminder>> loader, List<Reminder> data) {
        if (mAdapter != null) {
            mAdapter.setReminders(data);
            mAdapter.notifyDataSetChanged();

            mEmptyView.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Reminder>> loader) {
    }

    private class AddReminderClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
            startActivity(intent);
        }
    }

    private class ReminderClickListener implements OnRecyclerItemClickListener {
        @Override
        public void onItemClick(View v, int position) {
            Reminder r = mAdapter.getItem(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.finished_reminder_confirmation)
                    .setPositiveButton(R.string.remove_reminder, new ReminderActionListener(r))
                    .setNegativeButton(android.R.string.cancel, null);

            builder.show();
        }
    }

    /**
     * Click listener to handle removing a reminder.
     */
    private class ReminderActionListener implements DialogInterface.OnClickListener {

        private Reminder mReminder;

        public ReminderActionListener(Reminder r) {
            mReminder = r;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            // In case it has become a notification or is still a pending alarm, we'll just cancel both to be safe
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(mReminder.getId().intValue());

            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            intent.setAction(AlarmReceiver.ACTION);
            intent.putExtra(AlarmReceiver.EXTRA_REMINDER, mReminder.getId().longValue());
            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, mReminder.getId().intValue(), intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.cancel(sender);

            ReminderDao reminderDao = App.mDaoSession.getReminderDao();
            reminderDao.delete(mReminder);

            getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
        }
    }
}
