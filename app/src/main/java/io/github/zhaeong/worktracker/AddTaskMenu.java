package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



public class AddTaskMenu extends AppCompatActivity {
    public final static int RESULT_ADD = 10001;
    public final static int RESULT_EDIT = 10002;
    public final static int RESULT_DELETE = 10003;

    public final static String TASK_NAME_MENU = "com.example.addtaskmenu.TASKNAME";
    public final static String TASK_DESCRIPTION_MENU = "com.example.addtaskmenu.TASKDESC";
    public final static String TASK_POSITION_MENU = "com.example.addtaskmenu.TASKPOSIT";

    private int taskPosit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_menu);

        Intent intent = getIntent();
        String taskName = intent.getStringExtra(MainActivity.TASK_NAME);
        String taskDesc = intent.getStringExtra(MainActivity.TASK_DESCRIPTION);
        taskPosit = intent.getIntExtra(MainActivity.TASK_POSITION, -1);

        EditText editText_taskName = (EditText) findViewById(R.id.task_name);
        EditText editText_taskDesc = (EditText) findViewById(R.id.task_description);

        editText_taskName.setText(taskName);
        editText_taskDesc.setText(taskDesc);


    }

    public void addTask(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText_taskName = (EditText) findViewById(R.id.task_name);
        EditText editText_taskDesc = (EditText) findViewById(R.id.task_description);
        String taskName = editText_taskName.getText().toString();
        String taskDesc = editText_taskDesc.getText().toString();

        intent.putExtra(TASK_NAME_MENU, taskName);
        intent.putExtra(TASK_DESCRIPTION_MENU, taskDesc);
        intent.putExtra(TASK_POSITION_MENU, taskPosit);
        //Not an existing task, so create a new task
        if(taskPosit == -1) {
            setResult(RESULT_ADD, intent);
        }
        else
        {
            setResult(RESULT_EDIT, intent);
        }
        finish();
        }

    public void deleteTask(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_DELETE, intent);
        finish();
    }

    }

