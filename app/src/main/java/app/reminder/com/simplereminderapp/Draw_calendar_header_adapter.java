package app.reminder.com.simplereminderapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class is used to draw the calendar header containing the days name
 */
public class Draw_calendar_header_adapter extends BaseAdapter implements View.OnClickListener
{
    private final Context _context;
    private final List<String> days_list;
    private final String[] weekdays = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

    //   constructor
    public Draw_calendar_header_adapter(Context context, int textViewResourceId)
    {
        super();
        this._context = context;
        this.days_list = new ArrayList<String>();
        print_header();
    }

    public String getItem(int position)
    {
        return days_list.get(position);
    }

    @Override
    public int getCount()
    {
        return days_list.size();
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }

    private void print_header()
    {

        for(String weekday : weekdays)
            days_list.add(weekday);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View cell_view = convertView;
        if (null == cell_view) //if the cell view hasnt been initialized, then convert the grid_cell layout xml into view object
        {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cell_view = inflater.inflate(R.layout.grid_cell, parent, false);
        }

        //get the grid cell instance and set the text and color
        Button grid_cell = (Button) cell_view.findViewById(R.id.gridcell);
        grid_cell.setText(days_list.get(position));
        grid_cell.setTextColor(Color.BLUE);

        return cell_view;
    }

    @Override
    public void onClick(View view)
    {
            //
    }
}