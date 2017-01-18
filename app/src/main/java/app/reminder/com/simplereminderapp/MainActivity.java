package app.reminder.com.simplereminderapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
    private static final String tag = "MainActivity";
    private Button currentMonth;
    private Button selected_date;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendar_view;
    private GridView calendar_header_view;
    private Draw_calendar_adapter calendar_adapter;
    private Draw_calendar_header_adapter header_adapter;
    private Calendar local_calendar;
    private int day, month, year;
    private Activity current_activity;
    private TextView cal_view_show_list;
    private SimpleDateFormat dateFormat;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Date date = null;
        if(bundle != null)
        {
            String date_str = bundle.getString("task_date");

            dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try
            {
                date = dateFormat.parse(date_str);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        draw_calender(date);

    }

    public void draw_calender(Date date)
    {
        current_activity = this;

        setContentView(R.layout.calendar_view); //set the initial layout
        //get the current year, month and calendar
        local_calendar = Calendar.getInstance(Locale.getDefault());

        if(date instanceof Date)
        {
            local_calendar.setTime(date);
        }

        day = local_calendar.get(Calendar.DATE);
        month = local_calendar.get(Calendar.MONTH);
        year = local_calendar.get(Calendar.YEAR);

        //get the buttons and images by id
        calendar_view = (GridView) this.findViewById(R.id.calendar);
        calendar_header_view = (GridView) this.findViewById(R.id.calendarheader);
        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);

        //set the current date in the view
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentMonth = (Button) this.findViewById(R.id.currentMonth);
        currentMonth.setText(new SimpleDateFormat("MMMM").format(local_calendar.getTime()));

        selected_date = (Button)this.findViewById(R.id.selected_date);
        selected_date.setText(dateFormat.format(local_calendar.getTime()));
        //set the click listeners
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);


        // initialize the calendar view
        calendar_adapter = new Draw_calendar_adapter(getApplicationContext(), current_activity, R.id.gridcell,day, month, year);
        calendar_adapter.notifyDataSetChanged();
        calendar_view.setAdapter(calendar_adapter);

        //  Draw_calendar_header_adapter
        //init the cal header
        header_adapter = new Draw_calendar_header_adapter(getApplicationContext(), R.id.gridcell);
        header_adapter.notifyDataSetChanged();
        calendar_header_view.setAdapter(header_adapter);


        //set the task list button
        cal_view_show_list = (TextView)this.findViewById(R.id.cal_view_show_list);
        cal_view_show_list.setOnClickListener(this);

    }

    @Override
    public void onClick(View view_id) {

        String date_str = dateFormat.format(local_calendar.getTime());
        if (view_id.getId() == prevMonth.getId()) {
            if (month < 1) {
                month = 11;
                year--;
            } else {
                month--;
            }
        }
        if (view_id.getId() == nextMonth.getId()) {
            if (month >= 11) {
                month = 0;
                year++;
            } else {
                month++;
            }
        }

        // redraw the calender UI based on calculated day and year
        if(view_id.getId() == prevMonth.getId() || view_id.getId() == nextMonth.getId())
        {
            calendar_adapter = new Draw_calendar_adapter(getApplicationContext(), current_activity, R.id.gridcell, day, month, year);
            local_calendar.set(year, month, local_calendar.get(Calendar.DAY_OF_MONTH));
            selected_date.setText(date_str);
            currentMonth.setText(new SimpleDateFormat("MMMM").format(local_calendar.getTime()));
            calendar_adapter.notifyDataSetChanged();
            calendar_view.setAdapter(calendar_adapter);
        }

        if(view_id.getId() == cal_view_show_list.getId())
        {
            Intent addTask = new Intent(view_id.getContext(), Task_list.class);
            addTask.putExtra("task_date",dateFormat.format(Calendar.getInstance(Locale.getDefault()).getTime()));
            current_activity.startActivityForResult(addTask, 0);

        }
    }

}