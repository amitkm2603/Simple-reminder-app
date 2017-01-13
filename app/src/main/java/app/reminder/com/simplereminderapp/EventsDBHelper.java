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


    public boolean insertTask (String task_dttm, int task_priority, String task_description,
                                String task_reminder,String task_reminder_dttm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task_dttm", task_dttm);
        contentValues.put("task_priority", task_priority);
        contentValues.put("task_description", task_description);
        contentValues.put("task_reminder", task_reminder);
        contentValues.put("task_reminder_dttm", task_reminder_dttm);
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

    public boolean updateTask (Integer id, String task_dttm, int task_priority, String task_description,
                               String task_reminder,String task_reminder_dttm)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task_dttm", task_dttm);
        contentValues.put("task_priority", task_priority);
        contentValues.put("task_description", task_description);
        contentValues.put("task_reminder", task_reminder);
        contentValues.put("task_reminder_dttm", task_reminder_dttm);

        db.update(TASK_TABLE, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteTask (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASK_TABLE,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getDayTask() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
