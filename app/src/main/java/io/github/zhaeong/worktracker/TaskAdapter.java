package io.github.zhaeong.worktracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A custom adaptor for tasks
 * Created by Owen on 2017-01-24.
 */

public class TaskAdapter extends CursorAdapter{
    Context context;

    public TaskAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_task_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView taskRowname = (TextView)view.findViewById(R.id.taskName);

        // Extract properties from cursor
        String taskName = cursor.getString(cursor.getColumnIndexOrThrow(CustomDBHelper.TASKS_COL_NAME));

        taskRowname.setText(taskName);
    }

/*
    public View getView(int position, View convertView, ViewGroup parent) {

        TaskObject taskObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task_row, parent, false);
        }

        TextView taskRowname = (TextView)convertView.findViewById(R.id.taskName);
        taskRowname.setText(taskObj.Name);

        return convertView;

    }*/
}
