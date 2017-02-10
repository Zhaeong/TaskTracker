package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import io.github.zhaeong.worktracker.TaskConstructs.CustomDBHelper;
import io.github.zhaeong.worktracker.TaskConstructs.DayInfoAdapter;
import io.github.zhaeong.worktracker.TaskConstructs.DayTaskInfoAdapter;

public class DayInfoDesc extends AppCompatActivity {

    private long dayId = -1;

    private ListView mTaskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_desc);


        Intent intent = getIntent();

        TextView Text_dayName = (TextView) findViewById(R.id.dayName);
        TextView Text_wakeTime = (TextView) findViewById(R.id.wakeTime);
        TextView Text_sleepTime = (TextView) findViewById(R.id.sleepTime);
        TextView Text_timeEla = (TextView) findViewById(R.id.timeElapsed);



        dayId = intent.getLongExtra(DayInfo.DAY_INFO_ID, -1);

        if(dayId != -1)
        {
            Cursor curTask = MainActivity.myTaskDatabase.getItemInTable(dayId, CustomDBHelper.DAYS_TABLE_NAME);

            String dayName = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.DAYS_COL_NAME));

            Long l_taskWakeTime = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.DAYS_WAKETIME));
            Long l_taskSleepTime = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.DAYS_SLEEPTIME));

            Long timeElapsed = l_taskSleepTime - l_taskWakeTime;

            Date DayWakeTime = new Date(l_taskWakeTime);
            Date DaySleepTime = new Date(l_taskSleepTime);

            String DayWakeDateString = DateFormat.getDateTimeInstance().format(DayWakeTime);
            String DaySleepDateString = DateFormat.getDateTimeInstance().format(DaySleepTime);

            Text_dayName.setText(dayName);
            Text_wakeTime.setText(DayWakeDateString);
            Text_sleepTime.setText(DaySleepDateString);

            Text_timeEla.setText(DayInfoAdapter.convertLongToString(timeElapsed));
            populateList();

        }
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
                Intent intent = new Intent(DayInfoDesc.this, AddTaskMenu.class);
                //intent.putExtra(TASK_ID, id);

                startActivityForResult(intent, 1);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
