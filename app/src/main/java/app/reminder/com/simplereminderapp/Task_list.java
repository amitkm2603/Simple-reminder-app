package app.reminder.com.simplereminderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Task_list extends Activity implements View.OnClickListener
{

    private TasksDBHelper task_db_helper;
    private TextView current_date;
    private String date_str;
    private final int max_daily_difficulty = 400;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        date_str = bundle.getString("task_date");
        task_db_helper = new TasksDBHelper(this);
        generate_task_list(date_str);
    }

    public void generate_task_list(String date_str)
    {
        setContentView(R.layout.task_list);
        ArrayList<Task> task_list = task_db_helper.get_day_task_list(date_str);
        Button add_task_btn = (Button)this.findViewById(R.id.add_task_btn);
        Button back_to_cal_btn = (Button)this.findViewById(R.id.back_to_cal_btn);
        add_task_btn.setOnClickListener(this);
        back_to_cal_btn.setOnClickListener(this);
        current_date = (TextView)this.findViewById(R.id.task_list_date);
        current_date.setText("Showing task list for: "+date_str);

        if(task_list.size() == 0)
        {

        }
        else
        {

            // Adding header to the task list
//
//            TableLayout tableLayout=(TableLayout)this.findViewById(R.id.task_tbl_layout);
//            TableRow rowHeader = new TableRow(getApplicationContext());
//            rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
//            rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
//                    TableLayout.LayoutParams.WRAP_CONTENT));
//            String[] headerText={"Difficulty","Description","Edit","Delete"};
//            for(String c:headerText) {
//                TextView tv = new TextView(this);
//                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                        TableRow.LayoutParams.WRAP_CONTENT));
//                tv.setGravity(Gravity.CENTER);
//                tv.setTextSize(18);
//                tv.setPadding(5, 5, 5, 15);
//                tv.setText(c);
//                rowHeader.addView(tv);
//            }
//            tableLayout.addView(rowHeader);

            //populating the actual task list
            ListView list_view = (ListView)this.findViewById(R.id.task_list);
            Task_list_adapter task_list_adapter = new Task_list_adapter(getApplicationContext(),this,task_list,date_str);
            task_list_adapter.notifyDataSetChanged();
            list_view.setAdapter(task_list_adapter);

        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.add_task_btn:

                //check if daily diff has been reached, in not then display the add task activity
                if(check_daily_difficulty_reached(date_str))
                {
                    Intent addTask = new Intent(v.getContext(), Add_task.class);
                    addTask.putExtra("task_date",date_str);
                    addTask.putExtra("task_id",0);
                    this.startActivityForResult(addTask, 0);
                }
                break;
            case R.id.back_to_cal_btn:
                //display the calendar if back btn is pressed
                Intent back_cal = new Intent(v.getContext(), MainActivity.class);
                back_cal.putExtra("task_date",date_str);
                this.startActivityForResult(back_cal, 0);
                break;
        }

    }

    /*
    Checks if daily difficulty has been reached by querying the db. if it is reached,
    then search for three available days for next 60 days when the task can be added and display
    it as an alert
     */
    public boolean check_daily_difficulty_reached(String check_date)
    {
          if(task_db_helper.get_total_daily_difficulty(check_date) < max_daily_difficulty)
            return true;
        else
        {

            ArrayList<String> probable_dates = new ArrayList<>();
            int max_count = 1;
            while(probable_dates.size()<3)
            {
                if(max_count == 60)
                    break;
                try {
                    Date current_date_temp = dateFormat.parse(check_date);
                    Calendar c = Calendar.getInstance();
                    c.setTime(current_date_temp);
                    c.add(Calendar.DATE,max_count);
                    String temp_string = dateFormat.format(c.getTime());

                    if(task_db_helper.get_total_daily_difficulty(temp_string) < max_daily_difficulty)
                    {
                        probable_dates.add(temp_string);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                finally {
                    max_count++;
                }
            }

            String alert_message = "";
            if(probable_dates.size() == 0)
                alert_message = "You have reached the maximum daily difficulty! Please consider adding the task to some other day!";
            else
            {
                alert_message = "You have reached the maximum daily difficulty! Please consider adding tasks to the following days:\n";
                for(String temp_str : probable_dates)
                {
                    alert_message += "\n"+temp_str;
                }

            }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(alert_message)
                    .setNeutralButton("Ok",null).show();
            return false;
        }

    }
}
