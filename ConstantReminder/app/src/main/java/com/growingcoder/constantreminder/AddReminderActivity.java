package com.growingcoder.constantreminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.growingcoder.constantreminder.data.gen.Reminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Pierce
 * @since 10/10/2015.
 */
public class AddReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private TextView mDateView;
    private TextView mTimeView;
    private EditText mReminderNameView;

    private Calendar mReminderCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mReminderNameView = (EditText) findViewById(R.id.activity_add_reminder_edittext_name);
        mDateView = (TextView) findViewById(R.id.activity_add_reminder_textview_date);
        mTimeView = (TextView) findViewById(R.id.activity_add_reminder_textview_time);

        mReminderCalendar = Calendar.getInstance(TimeZone.getDefault());

        updateDate();
        updateTime();

        mDateView.setOnClickListener(new DateClickListener());
        mTimeView.setOnClickListener(new TimeClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        if (mReminderNameView.getText().toString().length() > 0) {
            Reminder reminder = new Reminder(null, mReminderNameView.getText().toString(), mReminderCalendar.getTimeInMillis());
            long reminderID = App.mDaoSession.getReminderDao().insert(reminder);
            createAlarm(reminderID, reminder.getDateTime());
            finish();
        } else {
            Toast.makeText(App.sAppContext, R.string.save_validation_error, Toast.LENGTH_LONG).show();
        }

    }

    private void createAlarm(long id, Long time) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION);
        intent.putExtra(AlarmReceiver.EXTRA_REMINDER, id);
        PendingIntent sender = PendingIntent.getBroadcast(this, (int)id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, sender);
    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        mDateView.setText(sdf.format(mReminderCalendar.getTime()));
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        mTimeView.setText(sdf.format(mReminderCalendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mReminderCalendar.set(Calendar.YEAR, year);
        mReminderCalendar.set(Calendar.MONTH, monthOfYear);
        mReminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDate();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mReminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mReminderCalendar.set(Calendar.MINUTE, minute);
        updateTime();
    }

    private class DateClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(AddReminderActivity.this, AddReminderActivity.this,
                    mReminderCalendar.get(Calendar.YEAR),
                    mReminderCalendar.get(Calendar.MONTH),
                    mReminderCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }

    private class TimeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TimePickerDialog dialog = new TimePickerDialog(AddReminderActivity.this, AddReminderActivity.this,
                    mReminderCalendar.get(Calendar.HOUR_OF_DAY), mReminderCalendar.get(Calendar.MINUTE), false);
            dialog.show();
        }
    }
}
