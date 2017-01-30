package app.reminder.com.simplereminderapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Task_notification_receiver extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";

    public static PendingIntent setupAlarm(Context context, Task task, Date alarm_dttm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent deleteIntent = getStartPendingIntent(context, task.getTask_description(), task.getId(), task.getTask_priority());
        alarmManager.cancel(deleteIntent);

        PendingIntent alarmIntent = getStartPendingIntent(context, task.getTask_description(), task.getId(), task.getTask_priority());

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_dttm.getTime(), alarmIntent);
        Log.d("Info", "Alarm added for task: "+ task.getId());
        return alarmIntent;

    }

    public static void deleteAlarm(Context context, Task task, Date alarm_dttm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent deleteIntent = getDeleteIntent(context, task.getTask_description(), task.getId(), task.getTask_priority());
        alarmManager.cancel(deleteIntent); //remove the alarm from the alarm manager

        //remove the notification from the notification bar
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setDeleteIntent(deleteIntent);
        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(task.getId(), builder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {
            serviceIntent = Task_notification_intent_service.createIntentStartNotificationService(context, intent.getStringExtra("description"),
                    intent.getIntExtra("task_id",0), intent.getIntExtra("difficulty",0));
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {
            serviceIntent = Task_notification_intent_service.createIntentDeleteNotification(context, intent.getStringExtra("description"),
                    intent.getIntExtra("task_id",0), intent.getIntExtra("difficulty",0));
        }

        if (serviceIntent != null) {
            startWakefulService(context, serviceIntent);
        }
    }

    private static PendingIntent getStartPendingIntent(Context context, String description, int task_id, int task_difficulty) {
        Intent intent = new Intent(context, Task_notification_receiver.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        intent.putExtra("task_id",task_id);
        intent.putExtra("description",description);
        intent.putExtra("difficulty",task_difficulty);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context, String description, int task_id, int task_difficulty) {
        Intent intent = new Intent(context, Task_notification_receiver.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        intent.putExtra("task_id",task_id);
        intent.putExtra("description",description);
        intent.putExtra("difficulty",task_difficulty);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
