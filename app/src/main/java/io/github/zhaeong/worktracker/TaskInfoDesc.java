package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import io.github.zhaeong.worktracker.TaskConstructs.CustomDBHelper;
import io.github.zhaeong.worktracker.TaskConstructs.TaskInfoAdapter;

public class TaskInfoDesc extends AppCompatActivity {

    private long taskID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_desc);


        Intent intent = getIntent();

        TextView Text_taskName = (TextView) findViewById(R.id.taskName);
        TextView Text_taskCreTime = (TextView) findViewById(R.id.taskDate);

        TextView Text_taskStartDate = (TextView) findViewById(R.id.startDate);
        TextView Text_taskEndDate = (TextView) findViewById(R.id.endDate);
        TextView Text_taskTimeElapsed = (TextView) findViewById(R.id.timeElapsed);


        taskID = intent.getLongExtra(TaskInfo.TASK_INFO_ID, -1);

        if(taskID != -1)
        {
            Cursor curTask = MainActivity.myTaskDatabase.getTask(taskID);
            String taskName = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_NAME));

            Long l_taskCreTime = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_CREATION_DATETIME));
            Long l_taskStartTime = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_START_DATETIME));
            Long l_taskEndTime = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_END_DATETIME));
            Long l_taskTimeEla = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_ELAPSED));



            Date TaskCreDate = new Date(l_taskCreTime);
            Date TaskStartDate = new Date(l_taskStartTime);
            Date TaskEndDate = new Date(l_taskEndTime);

            String TaskCreDateString = DateFormat.getDateTimeInstance().format(TaskCreDate);
            String TaskStartDateString = DateFormat.getDateTimeInstance().format(TaskStartDate);
            String TaskEndDateString = DateFormat.getDateTimeInstance().format(TaskEndDate);

            Text_taskName.setText(taskName);
            Text_taskCreTime.setText(TaskCreDateString);
            Text_taskStartDate.setText(TaskStartDateString);
            Text_taskEndDate.setText(TaskEndDateString);

            Text_taskTimeElapsed.setText(TaskInfoAdapter.convertLongToString(l_taskTimeEla));

        }
    }
}
