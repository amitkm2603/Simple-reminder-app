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

public final class NotificationServiceStarterReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null && action.equalsIgnoreCase(Task_notification_intent_service.ACTION_MARK_DONE))
        {
            mark_as_done(context, intent);
        }
        else {
                //reinitialize the alarms
                Manage_alarms.setup_alarms(context);
        }

    }


    void mark_as_done(Context context, Intent current_intent)
    {
        TasksDBHelper task_db_helper = new TasksDBHelper(context);
        int task_id = current_intent.getIntExtra("task_id",0);

        if(task_db_helper.mark_task_done(task_id) == 1)
        {
            Task _task = task_db_helper.getTaskData(task_id);
            //delete the alarm
            Manage_alarms.delete_alarm(context, _task);
            Toast.makeText(context,"marked Complete!",Toast.LENGTH_LONG).show();

        }


    }
}