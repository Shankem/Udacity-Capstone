package com.growingcoder.constantreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.growingcoder.constantreminder.data.gen.Reminder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ReminderAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_fab_add).setOnClickListener(new AddReminderClickListener());

        setupViews();

        /*TODO when we remove a reminder we should treat cancelling it before it happens differently than
         * when we do it if it has been set. Both cases we delete from DB, remove from list, but in one case
         * we will cancel the pending intent of the alarm, the other we'll remove the notification.*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            List<Reminder> reminders = App.mDaoSession.getReminderDao().loadAll();
            mAdapter.setReminders(reminders);
            mAdapter.notifyDataSetChanged();

            mEmptyView.setVisibility(reminders.size() == 0 ? View.VISIBLE : View.GONE);
        }
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

            //TODO some way to mark it complete/delete the notification
        }
    }
}
