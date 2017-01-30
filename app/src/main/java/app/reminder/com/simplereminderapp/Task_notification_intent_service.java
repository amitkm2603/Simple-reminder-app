package app.reminder.com.simplereminderapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

public class Task_notification_intent_service extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_DELETE = "ACTION_DELETE";
    public static final String ACTION_MARK_DONE = "ACTION_MARK_DONE";
    public static final String VIEW_TASK = "VIEW_TASK";

    public Task_notification_intent_service() {
        super(Task_notification_intent_service.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context, String description, int task_id, int difficulty) {
        Intent intent = new Intent(context, Task_notification_intent_service.class);
        intent.setAction(ACTION_START);
        intent.putExtra("task_id",task_id);
        intent.putExtra("description",description);
        intent.putExtra("difficulty",difficulty);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context, String description, int task_id, int difficulty) {
        Intent intent = new Intent(context, Task_notification_intent_service.class);
        intent.setAction(ACTION_DELETE);
        intent.putExtra("task_id",task_id);
        intent.putExtra("description",description);
        intent.putExtra("difficulty",difficulty);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification(intent);
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification(Intent original_intent) {

        String text_str = original_intent.getStringExtra("description");
        int task_id = original_intent.getIntExtra("task_id",0);
        int difficulty = original_intent.getIntExtra("difficulty",0);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Task Reminder!")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.background_material_dark))
                .setContentText("Task Difficulty "+difficulty)
                .setSmallIcon(R.drawable.app_icon);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(text_str);
        builder.setStyle(inboxStyle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                task_id,
                new Intent(this, Add_task.class).putExtra("task_id",task_id).setAction(VIEW_TASK),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Intent intent = new Intent(this, NotificationServiceStarterReceiver.class);
        intent.setAction(ACTION_MARK_DONE);
        intent.putExtra("task_id",task_id);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0, "Mark as done", pIntent);

        builder.setDeleteIntent(Task_notification_receiver.getDeleteIntent(this,text_str,task_id,difficulty));
        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(task_id, builder.build());
    }

}


