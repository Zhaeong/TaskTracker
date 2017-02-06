package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.zhaeong.worktracker.TaskConstructs.CustomDBHelper;
import io.github.zhaeong.worktracker.TaskConstructs.TaskAdapter;

import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_ADD;
import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_DELETE;
import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_EDIT;

public class MainActivity extends AppCompatActivity {


    private ListView mListView;
    public final static String TASK_NAME = "com.example.mainactivity.TASKNAME";
    public final static String TASK_DESCRIPTION = "com.example.mainactivity.TASKDESC";
    public final static String TASK_ID = "com.example.mainactivity.TASKID";

    //Database Variable
    public static CustomDBHelper myTaskDatabase;

    //Drawer Variables
    private ListView mDrawerList;
    private ArrayAdapter<String> mDrawerAdapter;
    private ArrayList<String> drawerItemsArray = new ArrayList<String>();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public final static String AddNew = "Add New Task";
    public final static String TaskInfoScreen = "Task Info Screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateDatabase();
        SetUpDrawer();
        SetUpFAB();
        RefreshView();
    }

    public void RefreshView()
    {
        populateList();
        setCurActiveTask();
    }

    //Set up to menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_sidemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        /*If click on icon
        if(item.getItemId() == R.id.action_help)
        {
            //do something when click on help
        }
        */
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    protected void setCurActiveTask()
    {
        TextView curActiveTask = (TextView)findViewById(R.id.activeTaskDisplay);
        Cursor cActiveTask = myTaskDatabase.getActiveTask();
        if(cActiveTask.getCount() == 1)
        {
            String taskName = cActiveTask.getString(cActiveTask.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_NAME));
            curActiveTask.setText(taskName);
        }
        else
        {
            curActiveTask.setText("No Active Task Currently");
        }
    }

    protected void initiateDatabase()
    {
        myTaskDatabase = new CustomDBHelper(this);
    }


    protected void SetUpDrawer()
    {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        drawerItemsArray.add(TaskInfoScreen);
        mDrawerList = (ListView)findViewById(R.id.right_drawer);

        mDrawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItemsArray);
        mDrawerList.setAdapter(mDrawerAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(drawerItemsArray.get(position).equals(AddNew))
                {
                    addNewTask(view);
                }
                else if(drawerItemsArray.get(position).equals(TaskInfoScreen))
                {
                    openTaskInfoScreen();
                }
                Toast.makeText(MainActivity.this, drawerItemsArray.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SetUpFAB()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addtaskFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTask(view);
            }
        });
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_help).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    protected void populateList()
    {
        final TaskAdapter adapter =
                new TaskAdapter(this, myTaskDatabase.getAllTasks());
        mListView = (ListView) findViewById(R.id.task_list);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddTaskMenu.class);
                intent.putExtra(TASK_ID, id);

                startActivityForResult(intent, 1);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //Called when the user clicks the AddTask button
    public void addNewTask(View view) {
        Intent intent = new Intent(this, AddTaskMenu.class);
        startActivityForResult(intent, 1);
    }

    public void openTaskInfoScreen()
    {
        Intent intent = new Intent(this, TaskInfo.class);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_ADD){

            }
            if(resultCode == RESULT_EDIT)
            {

            }
            if(resultCode == RESULT_DELETE)
            {

            }

            if (resultCode == RESULT_CANCELED) {
                // do something if there is no result

            }

            RefreshView();
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}
