package app.reminder.com.simplereminderapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Description: This class is used to draw the calendar individual cells
 */

public class Draw_calendar extends BaseAdapter implements View.OnClickListener
{
    private static final String tag = "Draw_calendar";
    private final Context _context;
    private final List<String> date_list;
    private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    private final int day, month, year;
    int daysInMonth, prevMonthDays;
    private final int currentDayOfMonth;
    private Button gridcell;
    private Context app_context;
    private Activity current_activity;
    private int trailingSpaces;
    private boolean print_current_date = false;

    // Days in Current Month
    public Draw_calendar(Context context, Activity current_act, int textViewResourceId, int day, int month, int year)
    {
        super();
        this._context = context;
        this.date_list = new ArrayList<String>();
        this.day = day;
        this.month = month;
        this.year = year;
        this.current_activity = current_act;

        Calendar calendar = Calendar.getInstance();
        currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//        currentDayOfMonth = day;

        if(calendar.get(Calendar.MONTH) == month)
            print_current_date = true;


        printMonth(month, year);
    }

    public String getItem(int position)
    {
        return date_list.get(position);
    }

    @Override
    public int getCount()
    {
        return date_list.size();
    }

    private void printMonth(int mm, int yy)
    {
        // The number of days to leave blank at
        // the start of this month.

        int leadSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;
        int currentMonth  = 0;

        // Days in Current Month
        daysInMonth = daysOfMonth[mm];

        currentMonth = mm;
        if (currentMonth == 11)
        {
            prevMonth = 10;
            daysInPrevMonth = daysOfMonth[prevMonth];
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
        } else if (currentMonth == 0)
        {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = daysOfMonth[prevMonth];
            nextMonth = 1;
        } else
        {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = daysOfMonth[prevMonth];
        }

        GregorianCalendar cal = new GregorianCalendar(yy, mm, currentDayOfMonth);
        GregorianCalendar cal_first_day = new GregorianCalendar(yy, mm, 1);
        cal_first_day.setFirstDayOfWeek(Calendar.MONDAY);

        // Compute how much to leave before before the first day of the
        // month.
        trailingSpaces = (cal_first_day.get(Calendar.DAY_OF_WEEK) +7-2 ) % 7 ;

        if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
        {
            ++daysInMonth;
        }

        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++)
        {
            date_list.add(String.valueOf((daysInPrevMonth - trailingSpaces + 1) + i) + "-GREY" + "-" + (prevMonth+1) + "-" + prevYear);
        }

        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++)
        {
            date_list.add(String.valueOf(i) + "-WHITE" + "-" + (mm+1) + "-" + yy);
        }

        // Leading Month days
        for (int i = 0; i < date_list.size() % 7; i++)
        {
            date_list.add(String.valueOf(i + 1) + "-GREY" + "-" + (nextMonth+1) + "-" + nextYear);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.grid_cell, parent, false);
        }

        gridcell = (Button) row.findViewById(R.id.gridcell);
        gridcell.setOnClickListener(this);

        // ACCOUNT FOR SPACING

        String[] day_color = date_list.get(position).split("-");
        gridcell.setText(day_color[0]);


        if(Integer.parseInt(day_color[2]) < 10)
            day_color[2] = "0"+day_color[2];


        gridcell.setTag(day_color[0] + "-" + day_color[2] + "-" + day_color[3]);

        if (day_color[1].equals("GREY"))
        {
            gridcell.setTextColor(Color.LTGRAY);
        }
        if (day_color[1].equals("WHITE"))
        {
            gridcell.setTextColor(Color.WHITE);
        }
        if (print_current_date && (position == ((currentDayOfMonth + trailingSpaces) - 1)))
        {
            gridcell.setTextColor(Color.BLUE);
        }

        return row;
    }

    @Override
    public void onClick(View view)
    {
        String current_date_temp = (String) view.getTag();
        Button currentMonth = (Button) current_activity.findViewById(R.id.currentMonth);
        currentMonth.setText(current_date_temp);

        //transfer the view to add task
        Intent addTask = new Intent(view.getContext(), Task_list.class);
        addTask.putExtra("task_date",current_date_temp);
        current_activity.startActivityForResult(addTask, 0);

    }
}