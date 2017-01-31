package io.github.zhaeong.worktracker;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_ADD;
import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_DELETE;
import static io.github.zhaeong.worktracker.AddTaskMenu.RESULT_EDIT;

public class MainActivity extends AppCompatActivity {


    private ListView mListView;
    public final static String TASK_NAME = "com.example.mainactivity.TASKNAME";
    public final static String TASK_DESCRIPTION = "com.example.mainactivity.TASKDESC";
    public final static String TASK_ID = "com.example.mainactivity.TASKID";
    public static CustomDBHelper myTaskDatabase;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> drawerItemsArray = new ArrayList<String>();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public final static String AddNew = "Add New Task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateDatabase();
        SetUpDrawer();
        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //If click on icon
        if(item.getItemId() == R.id.action_help)
        {
            //do something when click on help
        }
        return true;
    }

    protected void initiateDatabase()
    {
        myTaskDatabase = new CustomDBHelper(this);
    }


    protected void SetUpDrawer()
    {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        drawerItemsArray.add(AddNew);
        mDrawerList = (ListView)findViewById(R.id.right_drawer);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItemsArray);
        mDrawerList.setAdapter(mAdapter);

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(drawerItemsArray.get(position).equals(AddNew))
                {
                    addNewtask(view);
                }
                Toast.makeText(MainActivity.this, drawerItemsArray.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_help).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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
    public void addNewtask(View view) {
        Intent intent = new Intent(this, AddTaskMenu.class);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_ADD){
                String taskName = data.getStringExtra(AddTaskMenu.TASK_NAME_MENU);
                String taskDesc = data.getStringExtra(AddTaskMenu.TASK_DESCRIPTION_MENU);

                myTaskDatabase.addTask(taskName, taskDesc);
                populateList();
            }
            if(resultCode == RESULT_EDIT)
            {

                long taskPosit = data.getLongExtra(AddTaskMenu.TASK_ID_MENU, -1);
                if(taskPosit != -1)
                {
                    String taskName = data.getStringExtra(AddTaskMenu.TASK_NAME_MENU);
                    String taskDesc = data.getStringExtra(AddTaskMenu.TASK_DESCRIPTION_MENU);

                    myTaskDatabase.updateTask(taskPosit, taskName, taskDesc);
                    populateList();
                }
            }
            if(resultCode == RESULT_DELETE)
            {
                long taskPosit = data.getLongExtra(AddTaskMenu.TASK_ID_MENU, -1);
                myTaskDatabase.deleteTask(taskPosit);
                populateList();
            }

            if (resultCode == RESULT_CANCELED) {
                // do something if there is no result
            }
        }
    }
}
