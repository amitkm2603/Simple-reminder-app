package app.reminder.com.simplereminderapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {
    private static final String tag = "MainActivity";
    private Button currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendar_view;
    private GridView calendar_header_view;
    private Draw_calendar calendar_adapter;
    private Draw_calendar_header header_adapter;
    private Calendar local_calendar;
    private int day, month, year;
    private DateFormat dateFormat;
    private Activity current_activity;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Date date = new GregorianCalendar(2016, Calendar.NOVEMBER, 11).getTime();
        draw_calender(date);

    }

    public void draw_calender(Date date)
    {
        setContentView(R.layout.calendar_view); //set the initial layout

        current_activity = this;
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
        currentMonth.setText(dateFormat.format(local_calendar.getTime()));

        //set the click listeners
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);


        // initialize the calendar view
        calendar_adapter = new Draw_calendar(getApplicationContext(), current_activity, R.id.gridcell,day, month, year);
        calendar_adapter.notifyDataSetChanged();
        calendar_view.setAdapter(calendar_adapter);

        //  Draw_calendar_header
        //init the cal header
        header_adapter = new Draw_calendar_header(getApplicationContext(), R.id.gridcell);
        header_adapter.notifyDataSetChanged();
        calendar_header_view.setAdapter(header_adapter);
    }

    @Override
    public void onClick(View view_id) {
        //click handler with v = id of the btn
        System.out.println(view_id);
        ;
        if (view_id == prevMonth) {
            if (month < 1) {
                month = 11;
                year--;
            } else {
                month--;
            }
        }
        if (view_id == nextMonth) {
            if (month >= 11) {
                month = 0;
                year++;
            } else {
                month++;
            }
        }

        // redraw the calender UI based on calculated day and year
        if(view_id == prevMonth || view_id == nextMonth)
        {
            calendar_adapter = new Draw_calendar(getApplicationContext(), current_activity, R.id.gridcell, day, month, year);
            local_calendar.set(year, month, local_calendar.get(Calendar.DAY_OF_MONTH));
            currentMonth.setText(dateFormat.format(local_calendar.getTime()));
            calendar_adapter.notifyDataSetChanged();
            calendar_view.setAdapter(calendar_adapter);
        }
    }

}