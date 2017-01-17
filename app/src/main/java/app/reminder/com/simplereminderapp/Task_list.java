package app.reminder.com.simplereminderapp;

import android.app.Activity;
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

import java.util.ArrayList;

public class Task_list extends Activity implements View.OnClickListener
{

    private TasksDBHelper task_db_helper;
    private TextView current_date;
    private Button add_task_btn;
    private String date_str;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
//        date_str = bundle.getString("task_date");
        date_str = "21-01-2017";
        task_db_helper = new TasksDBHelper(this);
        generate_task_list(date_str);
    }

    public void generate_task_list(String date_str)
    {
        setContentView(R.layout.task_list);
        ArrayList<Task> task_list = task_db_helper.get_day_task_list(date_str);
        add_task_btn = (Button)this.findViewById(R.id.add_task_btn);
        add_task_btn.setOnClickListener(this);
//        Toast.makeText(getApplicationContext(),"aa"+task_list.size(),Toast.LENGTH_LONG).show();
        if(task_list.size() == 0)
        {

        }
        else
        {
            /*
            TableLayout tableLayout=(TableLayout)this.findViewById(R.id.task_tbl_layout);
            TableRow rowHeader = new TableRow(getApplicationContext());
            rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
            rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            String[] headerText={"Priority","Description"};
            for(String c:headerText) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(c);
                rowHeader.addView(tv);
            }
            tableLayout.addView(rowHeader);

            for(Task task: task_list)
            {
                int task_id= task.getId();
                String task_description = task.getTask_description();
                int task_priority = task.getTask_priority();

                TableRow row = new TableRow(getApplicationContext());
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                String[] colText={task_priority+"", task_description};
                for(String text:colText) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setText(text);
                    row.addView(tv);
                }
                tableLayout.addView(row);

            }
            */
            current_date = (TextView)this.findViewById(R.id.task_list_date);
            current_date.setText(date_str);
            TableLayout tableLayout=(TableLayout)this.findViewById(R.id.task_tbl_layout);
            TableRow rowHeader = new TableRow(getApplicationContext());
            rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
            rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            String[] headerText={"Difficulty","Description","Edit","Delete"};
            for(String c:headerText) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(5, 5, 5, 15);
                tv.setText(c);
                rowHeader.addView(tv);
            }
            tableLayout.addView(rowHeader);

            ListView list_view = (ListView)this.findViewById(R.id.task_list);
            Task_list_adapter task_list_adapter = new Task_list_adapter(getApplicationContext(),this,task_list,date_str);
            task_list_adapter.notifyDataSetChanged();
            list_view.setAdapter(task_list_adapter);

        }
    }
    @Override
    public void onClick(View v) {
        if( v == add_task_btn)
        {
            Intent addTask = new Intent(v.getContext(), Add_task.class);
            addTask.putExtra("task_date",date_str);
            addTask.putExtra("task_id",0);
            this.startActivityForResult(addTask, 0);
        }
    }
}
