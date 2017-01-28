package app.reminder.com.simplereminderapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Add_task extends Activity implements View.OnClickListener
{

    private Activity current_activity;
    private Context context;
    private TextView save_btn;
    private TextView close_btn;
    private TextView task_alarm_txt;
    private CheckBox add_reminder_chk;
    private CheckBox add_task_complete;
    private TextView add_reminder_date;
    private TextView add_reminder_time;
    private EditText task_description;
    private EditText task_priority;
    private TextView task_dttm;
    private TasksDBHelper task_db_helper;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private Calendar local_calendar = Calendar.getInstance(Locale.getDefault());
    private String date_str;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        date_str = bundle.getString("task_date");
        task_db_helper = new TasksDBHelper(this);
        generate_view(date_str, bundle.getInt("task_id"));

    }
    public void generate_view(String date_str, int task_id) {

        setContentView(R.layout.add_task);

        save_btn            = (TextView)this.findViewById(R.id.add_task_save);
        close_btn           = (TextView)this.findViewById(R.id.add_task_close);
        add_reminder_chk    = (CheckBox)this.findViewById(R.id.add_task_reminder_checkbox);
        add_task_complete   = (CheckBox)this.findViewById(R.id.add_task_complete);
        add_reminder_date   = (TextView)this.findViewById(R.id.set_reminder_date_txt);
        add_reminder_time   = (TextView)this.findViewById(R.id.set_reminder_time_txt);
        task_description    = (EditText)this.findViewById(R.id.add_task_text);
        task_priority       = (EditText)this.findViewById(R.id.add_task_priority);
        task_dttm           = (TextView)this.findViewById(R.id.add_task_date);
        task_alarm_txt      = (TextView)this.findViewById(R.id.task_alarm_txt);



        String current_time = timeFormat.format(local_calendar.getTime());

        try {
            local_calendar.setTime(dateTimeFormat.parse((date_str+" "+current_time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Task task;
        if( task_id > 0)
        {
            task_dttm.setText("Update task for: "+date_str);
            task_dttm.setTag(task_id);
            task = task_db_helper.getTaskData(task_id);
        }
        else
        {
            task_dttm.setText("Add task for: "+date_str);
            task_dttm.setTag(0);
            task = new Task(0, date_str, 10, "", "no","no", (date_str+" "+current_time));

        }

        set_current_vals(task);


        save_btn.setOnClickListener(this);
        close_btn.setOnClickListener(this);
        add_reminder_chk.setOnClickListener(this);
        add_reminder_date.setOnClickListener(this);
        add_reminder_time.setOnClickListener(this);

    }

    private void set_current_vals(Task task)
    {
        if(task == null)
            return;
        task_description.setText(task.getTask_description());
        task_priority.setText(String.valueOf(task.getTask_priority()));
        int alarm_visible;

        if(task.getTask_reminder()!= null && task.getTask_reminder().equalsIgnoreCase("yes"))
        {
            add_reminder_chk.setChecked(true);
            alarm_visible = TextView.VISIBLE;
        }
            else
        {
            add_reminder_chk.setChecked(false);
            alarm_visible = TextView.INVISIBLE;
        }

        if(task.getTask_complete()!=null && task.getTask_complete().equalsIgnoreCase("yes"))
            add_task_complete.setChecked(true);
        else
            add_task_complete.setChecked(false);

        add_reminder_date.setVisibility(alarm_visible);
        add_reminder_time.setVisibility(alarm_visible);
        task_alarm_txt.setVisibility(alarm_visible);

        String date_str, time_str;

        try
        {
            date_str = dateFormat.format(dateTimeFormat.parse(task.getTask_reminder_dttm()));
            time_str = timeFormat.format(dateTimeFormat.parse(task.getTask_reminder_dttm()));
        }
        catch (Exception e)
        {
            date_str = "--";
            time_str = "--";
        }

        add_reminder_time.setText(time_str);
        add_reminder_date.setText(date_str);


    }


    @Override
    public void onClick(View v) {


        if(v.getId() == add_reminder_chk.getId())
        {
            if(add_reminder_chk.isChecked())
            {
                add_reminder_date.setVisibility(TextView.VISIBLE);
                add_reminder_time.setVisibility(TextView.VISIBLE);
                task_alarm_txt.setVisibility(TextView.VISIBLE);
            }
            else
            {
                add_reminder_date.setVisibility(TextView.INVISIBLE);
                add_reminder_time.setVisibility(TextView.INVISIBLE);
                task_alarm_txt.setVisibility(TextView.INVISIBLE);
            }
        }

        else if(v.getId() == add_reminder_date.getId())
        {
            //creating date picker dialog by implementing the datepicker.onDateSetListener using anonymous class implementation
            DatePickerDialog  mdiDialog =new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    String month ;
                    if(monthOfYear < 9)
                        month = "0" + (monthOfYear+1);
                    else
                        month = ""+(monthOfYear + 1);

                    add_reminder_date.setText(year+"-"+month+"-"+dayOfMonth);
                }
            }, local_calendar.get(Calendar.YEAR), local_calendar.get(Calendar.MONTH), local_calendar.get(Calendar.DATE));
            mdiDialog.show();
        }

        else if( v.getId() == add_reminder_time.getId())
        {
            TimePickerDialog  timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            add_reminder_time.setText(hourOfDay+":"+minute);

                        }
                    }, local_calendar.get(Calendar.HOUR_OF_DAY), local_calendar.get(Calendar.MINUTE), true);

            timePickerDialog.show();
        }
        else if( v.getId() == save_btn.getId())
        {
            int task_id = (int)task_dttm.getTag();
            boolean task_saved = save_task(task_id);
            //if the task id is 0 then transfer the view to the task list since this is new task added
            if(task_saved && task_id == 0)
            {
                Intent addTask = new Intent(v.getContext(), Task_list.class);
                addTask.putExtra("task_date",date_str);
                this.startActivityForResult(addTask, 0);
            }
        }
        else if( v.getId() == close_btn.getId())
        {
            //transfer the view to task list
            Intent addTask = new Intent(v.getContext(), Task_list.class);
            addTask.putExtra("task_date",date_str);
            this.startActivityForResult(addTask, 0);
        }
    }

    public boolean save_task(int id)
    {

        String _task_dttm = date_str;
        int _task_priority = Integer.valueOf(task_priority.getText().toString()) ;
        String _task_description = task_description.getText().toString();
        String _task_reminder = add_reminder_chk.isChecked()?"yes":"no";
        String _task_complete = add_task_complete.isChecked()?"yes":"no";
        String _task_reminder_dttm = (String)add_reminder_date.getText() +" "+(String)add_reminder_time.getText();
        String return_message = "";
        boolean validate_flag = true;
        boolean return_flag = false;

        //validate
        if(_task_description.length() == 0)
        {
            return_message ="Please provide a description!";
            validate_flag = false;
        }

        if(validate_flag)
        {
            //validate if daily difficulty has been reached
            int current_difficulty = task_db_helper.get_total_daily_difficulty(_task_dttm, id)  ;
            boolean y = ( current_difficulty + _task_priority ) > Task.MAX_DAILY_DIFFICULTY;
            if( ( current_difficulty + _task_priority ) > Task.MAX_DAILY_DIFFICULTY )
            {
                return_message ="You have reached the maximum daily difficulty! Please select difficulty lower than "
                        +(Task.MAX_DAILY_DIFFICULTY - current_difficulty);
                validate_flag = false;
            }
        }

        if(validate_flag)
        {
            //create a task obj
            Task task = new Task(id, _task_dttm,_task_priority, _task_description, _task_complete, _task_reminder, _task_reminder_dttm);

            //if the id is 0, insert a new row in the db else update it
            if(id == 0)
            {
                if(task_db_helper.insertTask(task))
                {
                    return_message = "Task added successfully!";
                    return_flag = true;
                }
                else
                {
                    return_message = "Something went wrong! Unable to add the task.";
                }
            }
            else
            {
                if(task_db_helper.updateTask(task))
                {
                    return_message = "Task updated successfully!";
                    return_flag = true;
                }
                else
                {
                    return_message = "Something went wrong! Unable to add the task.";
                }
            }
        }


        Toast.makeText(getApplicationContext(),return_message,Toast.LENGTH_LONG).show();
        return return_flag;
    }
    /*
    When back button is pressed, this transfer the view to task list page
     */
    public void onBackPressed() {
        Intent addTask = new Intent(getApplicationContext(), Task_list.class);
        addTask.putExtra("task_date",date_str);
        this.startActivityForResult(addTask, 0);
    }
}

