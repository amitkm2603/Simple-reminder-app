package app.reminder.com.simplereminderapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class EventsDBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "Cal_tasks.db";
    public static  final String TASK_TABLE = "app_tasks";
    public static final String TASK_DTTM = "task_dttm";
    public static final String TASK_PRIORITY = "task_priority";
    public static final String TASK_DESCRIPTION = "task_description";
    public static final String TASK_REMINDER = "task_reminder";
    public static final String TASK_REMINDER_DTTM = "task_reminder_dttm";
    public static final String TASK_ID = "id";

    public EventsDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE app_tasks (id integer primary key, task_dttm TEXT, task_priority INT, task_description TEXT, " +
                "task_reminder TEXT, task_reminder_dttm TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS app_tasks");
        onCreate(db);
    }


        public boolean insertTask(Task task)
        {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_DTTM, task.getTask_dttm());
        contentValues.put(TASK_PRIORITY, task.getTask_priority());
        contentValues.put(TASK_DESCRIPTION, task.getTask_description());
        contentValues.put(TASK_REMINDER, task.getTask_reminder());
        contentValues.put(TASK_REMINDER_DTTM, task.getTask_reminder_dttm());
        db.insert(TASK_TABLE, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TASK_TABLE+" where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TASK_TABLE);
        return numRows;
    }

//    public boolean updateTask (Integer id, String task_dttm, int task_priority, String task_description,
//                               String task_reminder,String task_reminder_dttm)
    public boolean updateTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_DTTM, task.getTask_dttm());
        contentValues.put(TASK_PRIORITY, task.getTask_priority());
        contentValues.put(TASK_DESCRIPTION, task.getTask_description());
        contentValues.put(TASK_REMINDER, task.getTask_reminder());
        contentValues.put(TASK_REMINDER_DTTM, task.getTask_reminder_dttm());

        db.update(TASK_TABLE, contentValues, "id = ? ", new String[] { Integer.toString(task.getId()) } );
        return true;
    }

    public Integer deleteTask (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASK_TABLE,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Task> get_day_task_list(String task_dttm_str) {
        ArrayList<Task> array_list = new ArrayList<Task>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TASK_TABLE+" where task_dttm = ? ORDER BY "+TASK_PRIORITY+" DESC", new String[]{task_dttm_str} );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Task task_temp = new Task(
                    res.getColumnIndex(TASK_ID),
                    res.getString(res.getColumnIndex(TASK_DTTM)),
                    res.getColumnIndex(TASK_PRIORITY),
                    res.getString(res.getColumnIndex(TASK_DESCRIPTION)),
                    res.getString(res.getColumnIndex(TASK_REMINDER)),
                    res.getString(res.getColumnIndex(TASK_REMINDER_DTTM))
            );
            array_list.add(task_temp);
            res.moveToNext();
        }
        return array_list;
    }
}
