package com.growingcoder.constantreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_fab_add).setOnClickListener(new AddReminderClickListener());

        /*TODO when we remove a reminder we should treat cancelling it before it happens differently than
         * when we do it if it has been set. Both cases we delete from DB, remove from list, but in one case
         * we will cancel the pending intent of the alarm, the other we'll remove the notification.*/
    }

    private class AddReminderClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
            startActivity(intent);
        }
    }
}
