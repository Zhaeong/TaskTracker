package io.github.zhaeong.tasktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import io.github.zhaeong.tasktracker.TaskConstructs.DayTaskInfoAdapter;
import io.github.zhaeong.tasktracker.TaskConstructs.CustomDBHelper;

public class DayInfoDescView extends AppCompatActivity {

    private long dayId = -1;
    public final static int RESULT_DELETE = 20003;
    private ListView mTaskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_info_desc);

        setTitle("Day Description");
        Intent intent = getIntent();

        TextView Text_dayName = (TextView) findViewById(R.id.dayName);
        TextView Text_wakeTime = (TextView) findViewById(R.id.wakeTime);
        TextView Text_sleepTime = (TextView) findViewById(R.id.sleepTime);
        TextView Text_timeEla = (TextView) findViewById(R.id.timeElapsed);


        dayId = intent.getLongExtra(DayInfoView.DAY_INFO_ID, -1);

        if(dayId != -1)
        {
            Cursor curTask = MainActivity.myTaskDatabase.getItemInTable(dayId, CustomDBHelper.DAYS_TABLE_NAME);

            String dayName = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.DAYS_COL_NAME));

            long l_taskWakeTime = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.DAYS_WAKETIME));
            long l_taskSleepTime = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.DAYS_SLEEPTIME));

            long timeElapsed = 0;


            Date DayWakeTime = new Date(l_taskWakeTime);
            Date DaySleepTime = new Date(l_taskSleepTime);

            String DayWakeDateString = DateFormat.getDateTimeInstance().format(DayWakeTime);
            String DaySleepDateString = "Day is still active.";

            if(l_taskSleepTime != 0)
            {
                timeElapsed = l_taskSleepTime - l_taskWakeTime;
                DaySleepDateString = DateFormat.getDateTimeInstance().format(DaySleepTime);
            }

            Text_dayName.setText(dayName);
            Text_wakeTime.setText(DayWakeDateString);
            Text_sleepTime.setText(DaySleepDateString);

            Text_timeEla.setText(MainActivity.convertLongToString(timeElapsed));
            populateList();

        }
    }

    //Set up to menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dayinfo_sidemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //If click on icon


        if(item.getItemId() == R.id.deleteDay)
        {
            deleteTask();
        }
        finish();

        return true;
    }


    protected void populateList()
    {
        final DayTaskInfoAdapter adapter =
                new DayTaskInfoAdapter(this, MainActivity.myTaskDatabase.getAllTasksOfDay(dayId));
        mTaskListView = (ListView) findViewById(R.id.taskList);
        mTaskListView.setAdapter(adapter);

        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(DayInfoDescView.this, TaskInfoView.class);
                intent.putExtra(MainActivity.TASK_ID, id);

                startActivityForResult(intent, 1);
                adapter.notifyDataSetChanged();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            populateList();
        }
    }

    public void deleteTask() {
        Intent intent = new Intent(this, DayInfoView.class);
        MainActivity.myTaskDatabase.deleteDay(dayId);
        Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
        setResult(RESULT_DELETE, intent);
    }

}

