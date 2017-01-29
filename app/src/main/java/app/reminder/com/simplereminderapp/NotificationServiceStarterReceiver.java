package app.reminder.com.simplereminderapp;
//http://stackoverflow.com/questions/17673746/start-alarmmanager-if-device-is-rebooted
 /*
 * this class is used to invoke code when device is restarted
 */
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public final class NotificationServiceStarterReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == Task_notification_intent_service.ACTION_MARK_DONE)
        {
            mark_as_done(context, intent);
        }
        else {
            //get today's tasks
            //Calendar local_cal = Calendar.getInstance(Locale.getDefault());
            TasksDBHelper task_db_helper = new TasksDBHelper(context);
            Date date = new Date();
            String current_date = new SimpleDateFormat("dd-MM-yyyy").format(date);
            ArrayList<Task> task_list = task_db_helper.get_day_task_list(current_date);
            MainActivity.alarm_pending_intents.clear();
            if(task_list.size()>0)
            {

                for(Task _task: task_list)
                {
                    try
                    {
                        date = new SimpleDateFormat("dd-MM-yyyy H:m").parse(_task.getTask_reminder_dttm());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        continue;
                    }

                    PendingIntent pi =  Task_notification_receiver.setupAlarm(context, _task, date);
                    MainActivity.alarm_pending_intents.put(_task.getId(), pi);
                }
            }
        }

    }


    void mark_as_done(Context context, Intent current_intent)
    {
        TasksDBHelper task_db_helper = new TasksDBHelper(context);
        int task_id = current_intent.getIntExtra("task_id",0);
        Task _task = task_db_helper.getTaskData(task_id);
//        task_db_helper.mark_task_done(task_id);
//        Intent addTask = new Intent(context, Task_list.class);
//        addTask.putExtra("task_date",_task.getTask_dttm());
        Toast.makeText(context,"marked Complete!",Toast.LENGTH_LONG).show();
        try{
            //delete the alarm
            Task_notification_receiver.deleteAlarm(context, _task ,new SimpleDateFormat("dd-MM-yyyy H:m").parse(_task.getTask_reminder_dttm()) );
            //delete the object from the map
            MainActivity.alarm_pending_intents.remove(_task.getId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}