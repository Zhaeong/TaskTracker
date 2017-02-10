package io.github.zhaeong.tasktracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.zhaeong.tasktracker.TaskConstructs.CustomDBHelper;
import io.github.zhaeong.tasktracker.TaskConstructs.DayInfoAdapter;

public class DayInfoView extends AppCompatActivity {


    //List Variables
    private ListView mListView;

    //Drawer variables
    private DrawerLayout mDrawerLayout;
    private ArrayList<String> drawerItemsArray = new ArrayList<String>();
    private ListView mDrawerList;
    private ArrayAdapter<String> mDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    public final static String MainTaskScreen = "Task Tracker";
    public final static String DAY_INFO_ID = "com.example.taskinfo.TASKID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        setTitle("Day Info");
        SetUpDrawer();
        populateList();
    }

    //Set up to menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.dayinfo_sidemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //If click on icon
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        finish();

        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    protected void SetUpDrawer()
    {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.task_info);
        drawerItemsArray.add(MainTaskScreen);
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
                if(drawerItemsArray.get(position).equals(MainTaskScreen))
                {
                    gotoMainTaskScreen();
                }
                Toast.makeText(DayInfoView.this, drawerItemsArray.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gotoMainTaskScreen()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    protected void populateList()
    {
        final DayInfoAdapter adapter =
                new DayInfoAdapter(this, MainActivity.myTaskDatabase.getAllItemsInTable(CustomDBHelper.DAYS_TABLE_NAME));
        mListView = (ListView) findViewById(R.id.task_list_info);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(DayInfoView.this, DayInfoDescView.class);
                intent.putExtra(DAY_INFO_ID, id);

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


}
