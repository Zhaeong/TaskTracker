package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddTaskMenu extends AppCompatActivity {
    public final static int RESULT_ADD = 10001;
    public final static int RESULT_EDIT = 10002;
    public final static int RESULT_DELETE = 10003;

    public final static String TASK_NAME_MENU = "com.example.addtaskmenu.TASKNAME";
    public final static String TASK_DESCRIPTION_MENU = "com.example.addtaskmenu.TASKDESC";
    public final static String TASK_ID_MENU = "com.example.addtaskmenu.TASKID";

    private long taskID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_menu);

        Intent intent = getIntent();

        EditText editText_taskName = (EditText) findViewById(R.id.task_name);
        EditText editText_taskDesc = (EditText) findViewById(R.id.task_description);

        String taskName = "Name";
        String taskDesc = "Description";
        taskID = intent.getLongExtra(MainActivity.TASK_ID, -1);
        //Task already exist in database
        if(taskID != -1)
        {
            Cursor curTask = MainActivity.myTaskDatabase.getTask(taskID);
            taskName = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_NAME));
            taskDesc = curTask.getString(curTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_DESC));
            editText_taskName.setText(taskName);
            editText_taskDesc.setText(taskDesc);
        }

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
            saveTask(this.findViewById(R.id.activity_add_task_menu));
        }
        else if(item.getItemId() == R.id.deleteTask)
        {
            deleteTask(this.findViewById(R.id.activity_add_task_menu));
        }
        return true;
    }

    public void saveTask(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText_taskName = (EditText) findViewById(R.id.task_name);
        EditText editText_taskDesc = (EditText) findViewById(R.id.task_description);
        String taskName = editText_taskName.getText().toString();
        String taskDesc = editText_taskDesc.getText().toString();

        //Not an existing task, so create a new task
        if(taskID == -1) {
            MainActivity.myTaskDatabase.addTask(taskName, taskDesc);
            setResult(RESULT_ADD, intent);
            }
        else
        {
            MainActivity.myTaskDatabase.updateTask(taskID, taskName, taskDesc);
            setResult(RESULT_EDIT, intent);
            }
        finish();
        }

    public void deleteTask(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.myTaskDatabase.deleteTask(taskID);
        setResult(RESULT_DELETE, intent);
        finish();
        }

    }

