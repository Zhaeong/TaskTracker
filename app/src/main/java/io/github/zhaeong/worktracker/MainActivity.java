package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_ADD;
import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_DELETE;
import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_EDIT;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<TaskObject> TaskList = new ArrayList<TaskObject>();
    public final static String TASK_NAME = "com.example.mainactivity.TASKNAME";
    public final static String TASK_DESCRIPTION = "com.example.mainactivity.TASKDESC";
    public final static String TASK_POSITION = "com.example.mainactivity.TASKPOSIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateList();
    }

    protected void populateList()
    {
        final ArrayAdapter adapter =
                new TaskAdapter(this, TaskList);
        mListView = (ListView) findViewById(R.id.task_list);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TaskObject clickedTaskObj = (TaskObject) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, AddTaskMenu.class);
                intent.putExtra(TASK_NAME, clickedTaskObj.Name);
                intent.putExtra(TASK_DESCRIPTION, clickedTaskObj.Description);
                intent.putExtra(TASK_POSITION, position);

                startActivityForResult(intent, 1);
                adapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(),
                //        "position: " + position + " id:" + id + " name: " + clickedTaskObj.Name, Toast.LENGTH_LONG)
                //        .show();
            }
        });
    }

    //Called when the user clicks the AddTask button
    public void addNewtask(View view) {
        Intent intent = new Intent(this, AddTaskMenu.class);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_ADD){
                String taskName = data.getStringExtra(AddTaskMenu.TASK_NAME_MENU);
                String taskDesc = data.getStringExtra(AddTaskMenu.TASK_DESCRIPTION_MENU);

                TaskObject taskObj = new TaskObject(taskName, taskDesc);
                TaskList.add(taskObj);
                populateList();
            }
            if(resultCode == RESULT_EDIT)
            {

                int taskPosit = data.getIntExtra(AddTaskMenu.TASK_POSITION_MENU, -1);
                if(taskPosit != -1)
                {
                    String taskName = data.getStringExtra(AddTaskMenu.TASK_NAME_MENU);
                    String taskDesc = data.getStringExtra(AddTaskMenu.TASK_DESCRIPTION_MENU);

                    TaskObject modifiedTaskobj = TaskList.get(taskPosit);
                    modifiedTaskobj.Name = taskName;
                    modifiedTaskobj.Description = taskDesc;
                    TaskList.set(taskPosit, modifiedTaskobj);
                    populateList();
                }


            }
            if(resultCode == RESULT_DELETE)
            {

            }

            if (resultCode == RESULT_CANCELED) {
                // do something if there is no result
            }
        }
    }
}
