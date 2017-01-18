package app.reminder.com.simplereminderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
Adapter class for task list rows
 */
public class Task_list_adapter extends BaseAdapter implements View.OnClickListener
{

    private final Context context;
    private Activity current_activity;
    private final ArrayList<Task> task_list;
    private String current_date;


    public Task_list_adapter(Context _context, Activity _current_activity, ArrayList<Task> _task_list, String _current_date)
    {
        this.context = _context;
        this.current_activity = _current_activity;
        this.task_list = _task_list;
        this.current_date = _current_date;
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
        TextView task_edit_btn = (TextView)list_cell.findViewById(R.id.task_list_cell_edit);
        TextView task_delete_btn = (TextView)list_cell.findViewById(R.id.task_list_cell_delete);
        CheckBox task_complete_cb = (CheckBox)list_cell.findViewById(R.id.task_list_cell_mark_complete);


        Task current_task = task_list.get(position);

        //set values and tags
        task_difficulty.setText( String.valueOf(current_task.getTask_priority()) );
        task_description.setText(current_task.getTask_description());
        task_edit_btn.setTag(String.valueOf(current_task.getId()));
        task_delete_btn.setTag(String.valueOf(current_task.getId()));
        task_complete_cb.setTag(String.valueOf(current_task.getId()));

        if(current_task.getTask_complete()!= null && current_task.getTask_complete().equalsIgnoreCase("yes"))
            task_complete_cb.setChecked(true);
        else
            task_complete_cb.setChecked(false);

        //set onlick listeners
        task_edit_btn.setOnClickListener(this);
        task_delete_btn.setOnClickListener(this);
        task_complete_cb.setOnClickListener(this);
        return list_cell;
    }

    @Override
    public void onClick(View v) {
        int task_id = 0;
        switch (v.getId())
        {
            case R.id.task_list_cell_edit:

                task_id = Integer.valueOf(String.valueOf(v.getTag()));
                Intent addTask = new Intent(v.getContext(), Add_task.class);
                addTask.putExtra("task_date",current_date);
                addTask.putExtra("task_id",task_id);
                current_activity.startActivityForResult(addTask, 0);

                break;
            case R.id.task_list_cell_delete:
                task_id = Integer.valueOf(String.valueOf(v.getTag()));

                delete_confirmation(task_id);

                break;
            case R.id.task_list_cell_mark_complete:
                task_id = Integer.valueOf(String.valueOf(v.getTag()));
                toogle_mark_complete_confirmation(task_id,((CheckBox) v).isChecked());
                break;
        }
    }

    public void toogle_mark_complete_confirmation(final int task_id, final boolean marked) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        TasksDBHelper tasksDBHelper = new TasksDBHelper(context);
                        String toast_msg_str = "";
                        if(marked)
                        {
                            if(tasksDBHelper.mark_task_done(task_id) == 1)
                            {
                                current_activity.recreate();
                                toast_msg_str ="Task marked as complete!";
                            }
                            else
                            {
                                toast_msg_str ="Something went wrong. Unable to mark the task as complete!";
                            }
                        }
                        else
                        {
                            if(tasksDBHelper.unmark_task_done(task_id) == 1)
                            {
                                current_activity.recreate();
                                toast_msg_str ="Task marked as incomplete!";
                            }
                            else
                            {
                                toast_msg_str ="Something went wrong. Unable to mark the task as incomplete!";
                            }
                        }

                        Toast.makeText(current_activity, toast_msg_str, Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        break;
                }
            }
        };
        String dialog_msg = "";
        if(marked)
        {
            dialog_msg = "Are you sure you want to mark this task as complete?";
        }
        else
        {
            dialog_msg = "Are you sure you want to un-mark this task as complete?";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(current_activity);
        builder.setMessage(dialog_msg)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void delete_confirmation(final int task_id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                      TasksDBHelper tasksDBHelper = new TasksDBHelper(context);
                               if(tasksDBHelper.deleteTask(task_id) == 1)
                               {
                                   current_activity.recreate();
                                   Toast.makeText(current_activity, "Task deleted successfully!", Toast.LENGTH_LONG).show();
                               }
                        else
                               {
                                   Toast.makeText(current_activity, "Something went wrong. Unable to delete the Task.", Toast.LENGTH_LONG).show();
                               }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(current_activity);
        builder.setMessage("Are you sure that you want to delete this task?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
