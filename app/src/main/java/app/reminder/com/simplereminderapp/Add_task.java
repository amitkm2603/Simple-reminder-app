package app.reminder.com.simplereminderapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class Add_task extends BaseAdapter implements View.OnClickListener
{

    private Activity current_activity;
    private Context context;
    private final int task_id;

    public Add_task(Activity _current_activity, Context _context, int _task_id)
    {
        super();
        this.context = _context;
        this.current_activity = _current_activity;
        this.task_id = _task_id;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount() {
        return 0;
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
        return null;
    }
}

