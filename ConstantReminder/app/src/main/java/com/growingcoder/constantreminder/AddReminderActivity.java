package com.growingcoder.constantreminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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

        //TODO add menu with save

        mReminderNameView = (EditText) findViewById(R.id.activity_add_reminder_edittext_name);
        mDateView = (TextView) findViewById(R.id.activity_add_reminder_textview_date);
        mTimeView = (TextView) findViewById(R.id.activity_add_reminder_textview_time);

        mReminderCalendar = Calendar.getInstance(TimeZone.getDefault());

        updateDate();
        updateTime();

        mDateView.setOnClickListener(new DateClickListener());
        mTimeView.setOnClickListener(new TimeClickListener());
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
