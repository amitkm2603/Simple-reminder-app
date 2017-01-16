package app.reminder.com.simplereminderapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by User on 2017-01-16.
 */

public class Task_list extends Activity implements View.OnClickListener
{

    private EventsDBHelper task_db_helper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String date_str = bundle.getString("task_date");
        task_db_helper = new EventsDBHelper(this);
        generate_task_list(date_str);
    }

    public void generate_task_list(String date_str)
    {
        ArrayList<Task> task_list = task_db_helper.get_day_task_list(date_str);

        if(task_list.size() == 0)
        {

        }
        else
        {

        }
    }
    @Override
    public void onClick(View v) {

    }
}
