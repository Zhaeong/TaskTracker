package io.github.zhaeong.tasktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;

import io.github.zhaeong.tasktracker.R;
import io.github.zhaeong.tasktracker.TaskConstructs.CustomDBHelper;

public class TaskInfoView extends AppCompatActivity {

    public final static int RESULT_EDIT = 20002;
    public final static int RESULT_DELETE = 20003;

    private long taskID = -1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addtask_sidemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //If click on icon
        if(item.getItemId() == R.id.saveTask)
        {
            //do something when click on help
            saveTask();
        }
        else if(item.getItemId() == R.id.deleteTask)
        {
            deleteTask();
        }
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_view);
        setTitle("Task Information");

        Intent intent = getIntent();

        EditText editText_taskName = (EditText) findViewById(R.id.task_name);
        EditText editText_taskDesc = (EditText) findViewById(R.id.task_description);

        TextView creDate = (TextView)  findViewById(R.id.creDate);
        TextView finDate = (TextView)  findViewById(R.id.finDate);
        TextView timeLogged = (TextView)  findViewById(R.id.timeLogged);

        String taskName;
        String taskDesc;
        taskID = intent.getLongExtra(MainActivity.TASK_ID, -1);

        //Task already exist in database
        if(taskID != -1)
        {
            Cursor curTask = MainActivity.myTaskDatabase.getItemInTable(taskID, CustomDBHelper.TASKS_TABLE_NAME);
            taskName = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_NAME));
            taskDesc = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_DESC));

            long l_creDate = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_START_DATETIME));
            long l_finDate = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_END_DATETIME));
            long l_timeLogged = curTask.getLong(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_ELAPSED));

            String DayCreDate = DateFormat.getDateTimeInstance().format(l_creDate);
            String DayFinDate = "Task is active.";
            String s_TimeLogged = MainActivity.convertLongToString(l_timeLogged);

            if(l_finDate != 0)
            {
                DayFinDate = DateFormat.getDateTimeInstance().format(l_finDate);
            }

            editText_taskName.setText(taskName);
            editText_taskDesc.setText(taskDesc);

            creDate.setText(DayCreDate);
            finDate.setText(DayFinDate);
            timeLogged.setText(s_TimeLogged);
        }

    }

    public long saveTask() {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText_taskName = (EditText) findViewById(R.id.task_name);
        EditText editText_taskDesc = (EditText) findViewById(R.id.task_description);
        String taskName = editText_taskName.getText().toString();
        String taskDesc = editText_taskDesc.getText().toString();

        long TaskId = -1;

        //Not an existing task, so create a new task
        if(taskID != -1) {
            TaskId = taskID;
            MainActivity.myTaskDatabase.updateTask(taskID, taskName, taskDesc);
            Toast.makeText(getApplicationContext(), "Task Saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_EDIT, intent);
        }

        return TaskId;
    }

    public void deleteTask() {
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.myTaskDatabase.deleteTask(taskID);
        Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
        setResult(RESULT_DELETE, intent);
    }


}
