package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> ListValues = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateList();
    }

    protected void populateList()
    {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ListValues);
        mListView = (ListView) findViewById(R.id.task_list);
        mListView.setAdapter(adapter);
    }



    //Called when the user clicks the AddTask button
    public void addNewtask(View view) {
        Intent intent = new Intent(this, AddTaskMenu.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent, 1);
        //startActivity(intent);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String message = data.getStringExtra(AddTaskMenu.EXTRA_MESSAGE);
                ListValues.add(message);
            }
            if (resultCode == RESULT_CANCELED) {
                // do something if there is no result
            }
        }
    }
}
