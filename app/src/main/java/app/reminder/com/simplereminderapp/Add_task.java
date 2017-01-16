package app.reminder.com.simplereminderapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Add_task extends Activity implements View.OnClickListener
{

    private Activity current_activity;
    private Context context;
    private int task_id;
    private TextView save_btn;
    private TextView close_btn;
    private TextView task_alarm_txt;
    private CheckBox add_reminder_chk;
    private TextView add_reminder_date;
    private TextView add_reminder_time;
    private EditText task_description;
    private EditText task_priority;
    private TextView task_dttm;
    private EventsDBHelper task_db_helper;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private Calendar local_calendar = Calendar.getInstance(Locale.getDefault());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Date date = new GregorianCalendar(2016, Calendar.NOVEMBER, 11).getTime();
        Bundle bundle = getIntent().getExtras();
        String date_str = bundle.getString("task_date");
        task_db_helper = new EventsDBHelper(this);
        generate_view(date_str);

    }
    public void generate_view(String date_str) {

        setContentView(R.layout.add_task);

        save_btn            = (TextView)this.findViewById(R.id.add_task_save);
        close_btn           = (TextView)this.findViewById(R.id.add_task_close);
        add_reminder_chk    = (CheckBox)this.findViewById(R.id.add_task_reminder_checkbox);
        add_reminder_date   = (TextView)this.findViewById(R.id.set_reminder_date_txt);
        add_reminder_time   = (TextView)this.findViewById(R.id.set_reminder_time_txt);
        task_description    = (EditText)this.findViewById(R.id.add_task_text);
        task_priority       = (EditText)this.findViewById(R.id.add_task_priority);
        task_dttm           = (TextView)this.findViewById(R.id.add_task_date);
        task_alarm_txt      = (TextView)this.findViewById(R.id.task_alarm_txt);

        task_dttm.setText(date_str);

//        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String current_date = dateFormat.format(local_calendar.getTime());

//        dateFormat = new SimpleDateFormat("HH:mm");
        String current_time = timeFormat.format(local_calendar.getTime());
        Task task = null;
        if( task_id > 0)
        {
            task_dttm.setTag(task_id);
            //get the existing data and set it
        }
        else
        {
            task_dttm.setTag(0);
            task = new Task(0, current_date, 10, "", "no", (current_date+" "+current_time));

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
        task_priority.setText(Integer.valueOf(task.getTask_priority()).toString() );
        if(task.getTask_reminder() == "yes")
            add_reminder_chk.setChecked(true);
        else
            add_reminder_chk.setChecked(false);

        add_reminder_date.setVisibility(TextView.VISIBLE);
        add_reminder_time.setVisibility(TextView.VISIBLE);
        task_alarm_txt.setVisibility(TextView.VISIBLE);

        String date_str = "";
        String time_str = "";
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
    public boolean save_task()
    {

        int id = (int)task_dttm.getTag();
        String _task_dttm = (String)task_dttm.getText();
        int _task_priority = Integer.valueOf(task_priority.getText().toString()) ;
        String _task_description = task_description.getText().toString();
        String _task_reminder = add_reminder_chk.isChecked()?"yes":"no";
        String _task_reminder_dttm = (String)add_reminder_date.getText() +" "+(String)add_reminder_time.getText();


        //validate
        if(_task_description.length() == 0)
        {
            Toast.makeText(getApplicationContext(),"Please provide a description!",Toast.LENGTH_LONG).show();
            return false;
        }
        //create a task obj
        Task task = new Task(id, _task_dttm,_task_priority, _task_description, _task_reminder, _task_reminder_dttm);

        //if the id is 0, insert a new row in the db else update it
        if(id == 0)
            return  task_db_helper.insertTask(task);
        else
            return  task_db_helper.updateTask(task);

    }

    @Override
    public void onClick(View v) {


        if(v == add_reminder_chk)
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

        else if(v == add_reminder_date)
        {
            DatePickerDialog  mdiDialog =new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    String month = new String();
                    if(monthOfYear < 9)
                        month = "0" + (monthOfYear+1);
                    else
                        month = ""+(monthOfYear + 1);

                    add_reminder_date.setText(year+"-"+month+"-"+dayOfMonth);
                }
            }, local_calendar.get(Calendar.YEAR), local_calendar.get(Calendar.MONTH), local_calendar.get(Calendar.DATE));
            mdiDialog.show();
        }

        else if( v == add_reminder_time)
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
        else if( v == save_btn)
        {
            if(save_task())
            {
                 Toast.makeText(getApplicationContext(),"Task added successfully!",Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(),"aa"+task_db_helper.numberOfRows(),Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Something went wrong! Unable to add the task.",Toast.LENGTH_LONG).show();
            }
        }
    }
}

