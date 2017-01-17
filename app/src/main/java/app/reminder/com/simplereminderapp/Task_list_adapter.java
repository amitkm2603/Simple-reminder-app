package app.reminder.com.simplereminderapp;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 2017-01-17.
 */

public class Task_list_adapter extends BaseAdapter
{

    private final Context context;
    private Activity current_activity;
    private final ArrayList<Task> task_list;

    public Task_list_adapter(Context _context, Activity _current_activity, ArrayList<Task> _task_list)
    {
        this.context = _context;
        this.current_activity = _current_activity;
        this.task_list = _task_list;
    }
    @Override
    public int getCount() {
        return task_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list_cell = convertView;
        if (list_cell == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            list_cell = inflater.inflate(R.layout.task_list_cell, parent, false);
        }

        TextView task_difficulty = (TextView)list_cell.findViewById(R.id.task_list_cell_difficulty);
        TextView task_description = (TextView)list_cell.findViewById(R.id.task_list_cell_description);
        Button   task_edit_btn = (Button)list_cell.findViewById(R.id.task_list_cell_edit);
        ImageButton task_delete_btn = (ImageButton)list_cell.findViewById(R.id.task_list_cell_delete);


//        task_difficulty.setGravity(Gravity.CENTER);
//        task_difficulty.setTextSize(18);
//        task_difficulty.setPadding(5, 5, 5, 5);
//
//        task_description.setGravity(Gravity.CENTER);
//        task_description.setTextSize(18);
//        task_description.setPadding(5, 5, 5, 5);
//
//        task_edit_btn.setGravity(Gravity.CENTER);
//        task_edit_btn.setTextSize(18);
//        task_edit_btn.setPadding(5, 5, 5, 5);
//
//        task_delete_btn.setPadding(5, 5, 5, 5);

        Task current_task = task_list.get(position);

        //set values and tags
        task_difficulty.setText( String.valueOf(current_task.getTask_priority()) );
        task_description.setText(current_task.getTask_description());
        task_edit_btn.setTag(String.valueOf(current_task.getId()));
        task_delete_btn.setTag(String.valueOf(current_task.getId()));
        return list_cell;
    }
}
