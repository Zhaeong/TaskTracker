package io.github.zhaeong.tasktracker.TaskConstructs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import io.github.zhaeong.tasktracker.R;

/**
 * A custom adaptor for tasks
 * Created by Owen on 2017-01-24.
 */

public class DayInfoAdapter extends CursorAdapter{
    Context context;

    public DayInfoAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_dayinfo_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView taskRowName = (TextView)view.findViewById(R.id.dayName);

        // Extract properties from cursor
        String taskName = cursor.getString(cursor.getColumnIndexOrThrow(CustomDBHelper.DAYS_COL_NAME));


        taskRowName.setText(taskName);
    }
}
