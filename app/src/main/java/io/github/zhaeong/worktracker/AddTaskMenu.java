package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import io.github.zhaeong.worktracker.TaskConstructs.CustomDBHelper;

public class AddTaskMenu extends AppCompatActivity {
    public final static int RESULT_ADD = 10001;
    public final static int RESULT_EDIT = 10002;
    public final static int RESULT_DELETE = 10003;

    private long taskID = -1;

    private ToggleButton mTaskActivationToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_menu);

        mTaskActivationToggle =  (ToggleButton)findViewById(R.id.taskActivationButton);

        Intent intent = getIntent();

        EditText editText_taskName = (EditText) findViewById(R.id.task_name);
        EditText editText_taskDesc = (EditText) findViewById(R.id.task_description);

        String taskName;
        String taskDesc;
        int isActive;
        taskID = intent.getLongExtra(MainActivity.TASK_ID, -1);

        //Task already exist in database
        if(taskID != -1)
        {
            Cursor curTask = MainActivity.myTaskDatabase.getTask(taskID);
            taskName = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_NAME));
            taskDesc = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_DESC));
            isActive = curTask.getInt(curTask.getColumnIndexOrThrow(CustomDBHelper.TASK_IS_ACTIVE));
            editText_taskName.setText(taskName);
            editText_taskDesc.setText(taskDesc);
            mTaskActivationToggle.setChecked(isActive == 1);
        }
        setUpToggleButton();

    }

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

    public void setUpToggleButton()
    {
        mTaskActivationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(taskID != -1) // Task exists already
                {
                    if (isChecked) {
                        MainActivity.myTaskDatabase.TaskActivation(taskID, 1);
                        Toast.makeText(getApplicationContext(), "Task Activated", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        {
                        MainActivity.myTaskDatabase.TaskActivation(taskID, 0);
                        Toast.makeText(getApplicationContext(), "Task Deactivated", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Long savedTaskId = saveTask();
                    MainActivity.myTaskDatabase.TaskActivation(savedTaskId, 1);
                    finish();
                }
            }
        });
    }

    public long saveTask() {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText_taskName = (EditText) findViewById(R.id.task_name);
        EditText editText_taskDesc = (EditText) findViewById(R.id.task_description);
        String taskName = editText_taskName.getText().toString();
        String taskDesc = editText_taskDesc.getText().toString();

        long TaskId;

        //Not an existing task, so create a new task
        if(taskID == -1) {
            TaskId = MainActivity.myTaskDatabase.addTask(taskName, taskDesc);
            Toast message = new Toast(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_SHORT).show();
            setResult(RESULT_ADD, intent);
            }
        else{
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

