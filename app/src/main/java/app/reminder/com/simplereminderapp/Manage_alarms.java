package app.reminder.com.simplereminderapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Manage_alarms {
    public static HashMap<Integer,PendingIntent> alarm_pending_intents = new HashMap<>();
    private static TasksDBHelper task_db_helper = null;

    public static void setup_alarms(Context context)
    {
        if(task_db_helper == null)
            task_db_helper = new TasksDBHelper(context);

        Date date = new Date();
        String current_date = new SimpleDateFormat("dd-MM-yyyy").format(date);
        ArrayList<Task> task_list = task_db_helper.get_pending_alarm_task_list(current_date);
        alarm_pending_intents.clear();
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
                alarm_pending_intents.put(_task.getId(), pi);
                Log.d("Info", "Manage Alarms.setup_alarms: Alarm added for task: "+_task.getId()+" Time: "+_task.getTask_reminder_dttm());
            }
        }

    }

    public static void add_alarm(Context context, Task _task)
    {
        //if the task reminder isnot set to yes, do not add the alarm
        String x = _task.getTask_reminder();
        String z = _task.getTask_reminder();
        if(! _task.getTask_reminder().equalsIgnoreCase("yes"))
            return;

        //check if task is already added
        if(alarm_pending_intents.get(_task.getId()) instanceof PendingIntent)
        {
            //if already present, remove the alarm as its an update request
            delete_alarm(context,_task);
            Log.d("Info", "Manage_alarms.add_alarm: Task already exists. Deleting the previous entry. Task id: "+_task.getId());
        }

        //add the alarm
        try
        {
            //format the date string to date object
            Date date = new SimpleDateFormat("dd-MM-yyyy H:m").parse(_task.getTask_reminder_dttm());
            //setup the alarm and store the PI in the hashmap for future references
            PendingIntent pi =  Task_notification_receiver.setupAlarm(context, _task, date);
            alarm_pending_intents.put(_task.getId(), pi);
            Log.d("Info", "Manage Alarms.add_alarm: Alarm added for task: "+_task.getId()+" Time: "+_task.getTask_reminder_dttm());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void delete_alarm(Context context, Task _task)
    {
        PendingIntent deleteIntent = alarm_pending_intents.get(_task.getId());
        if( deleteIntent instanceof PendingIntent)
        {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(deleteIntent); //remove the alarm from the alarm manager

            //remove the notification from the notification bar
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setDeleteIntent(deleteIntent);

            final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(_task.getId(), builder.build());

            Log.d("Info", "Manage Alarms.delete_alarm: Alarm deleted for task: "+_task.getId());
        }
    }

}
