package io.github.zhaeong.worktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



public class AddTaskMenu extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.addtaskmenu.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_menu);
    }

    public void addTask(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText = (EditText) findViewById(R.id.task_title);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        setResult(RESULT_OK, intent);
        finish();
        }
    }

