package io.github.zhaeong.worktracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A custom adaptor for tasks
 * Created by Owen on 2017-01-24.
 */

public class TaskAdapter extends ArrayAdapter<TaskObject>{
    Context context;

    public TaskAdapter(Context context, ArrayList<TaskObject> taskObjects)
    {
        super(context, 0, taskObjects);
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        TaskObject taskObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task_row, parent, false);
        }

        TextView taskRowname = (TextView)convertView.findViewById(R.id.taskName);
        taskRowname.setText(taskObj.Name);

        return convertView;

    }
}
