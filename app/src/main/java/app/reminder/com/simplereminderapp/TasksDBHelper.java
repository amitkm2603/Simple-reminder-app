package app.reminder.com.simplereminderapp;
/*
SQLite helper class for the tasks table
 */
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class TasksDBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "Cal_tasks.db";
    private static  final String TASK_TABLE = "app_tasks";
    private static final String TASK_DTTM = "task_dttm";
    private static final String TASK_PRIORITY = "task_priority";
    private static final String TASK_DESCRIPTION = "task_description";
    private static final String TASK_COMPLETE = "task_complete";
    private static final String TASK_REMINDER = "task_reminder";
    private static final String TASK_REMINDER_DTTM = "task_reminder_dttm";
    private static final String TASK_ID = "id";
    private final static int    DB_VERSION = 2;

    public TasksDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE app_tasks (id integer primary key, task_dttm TEXT, task_priority INT, task_description TEXT, task_complete TEXT, " +
                "task_reminder TEXT, task_reminder_dttm TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            final String ALTER_TBL =
                    "ALTER TABLE " + TASK_TABLE +
                            " ADD COLUMN task_complete text;";
            db.execSQL(ALTER_TBL);
        }

    }


    public boolean insertTask(Task task)
        {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_DTTM, task.getTask_dttm());
        contentValues.put(TASK_PRIORITY, task.getTask_priority());
        contentValues.put(TASK_DESCRIPTION, task.getTask_description());
        contentValues.put(TASK_COMPLETE, task.getTask_complete());
        contentValues.put(TASK_REMINDER, task.getTask_reminder());
        contentValues.put(TASK_REMINDER_DTTM, task.getTask_reminder_dttm());
        db.insert(TASK_TABLE, null, contentValues);
        return true;
    }

    /*
    Returns a single row
     */
    public Task getTaskData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        Task task_temp = null;
        try{

            res = db.rawQuery( "select * from "+TASK_TABLE+" where id="+id+"", null );

            if(res.getCount() > 0) {

                res.moveToFirst();
                task_temp = new Task(
                    Integer.valueOf(res.getString(res.getColumnIndex(TASK_ID))),
                    res.getString(res.getColumnIndex(TASK_DTTM)),
                    Integer.valueOf(res.getString(res.getColumnIndex(TASK_PRIORITY))),
                    res.getString(res.getColumnIndex(TASK_DESCRIPTION)),
                    res.getString(res.getColumnIndex(TASK_COMPLETE)),
                    res.getString(res.getColumnIndex(TASK_REMINDER)),
                    res.getString(res.getColumnIndex(TASK_REMINDER_DTTM))
                );
            }

            return task_temp;
        }finally {

            res.close();
        }
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TASK_TABLE);
        return numRows;
    }


    public boolean updateTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_DTTM, task.getTask_dttm());
        contentValues.put(TASK_PRIORITY, task.getTask_priority());
        contentValues.put(TASK_DESCRIPTION, task.getTask_description());
        contentValues.put(TASK_COMPLETE, task.getTask_complete());
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


    public Integer get_total_daily_difficulty(String task_dttm_str)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        int task_priority_total = 0;
        try{
            res = db.rawQuery( "select sum(task_priority) as task_priority_total from "+TASK_TABLE+" where task_dttm="+task_dttm_str+"" +
                    " and task_complete = 'no'", null );

            if(res.getCount() > 0) {

                res.moveToFirst();
                task_priority_total = res.isNull(res.getColumnIndex("task_priority_total"))? 0: Integer.valueOf(res.getString(res.getColumnIndex("task_priority_total")));
            }

            return task_priority_total;
        }finally {

            res.close();
        }
    }


    public ArrayList<Task> get_day_task_list(String task_dttm_str) {
        ArrayList<Task> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TASK_TABLE+" where task_dttm = ? ORDER BY case "+TASK_COMPLETE+"\n" +
                " when \"yes\" then 3\n" +
                " when \"no\" then 2\n" +
                " when NULL then 1\n" +
                " end asc, "+TASK_PRIORITY+" DESC", new String[]{task_dttm_str} );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Task task_temp = new Task(
                    Integer.valueOf(res.getString(res.getColumnIndex(TASK_ID))),
                    res.getString(res.getColumnIndex(TASK_DTTM)),
                    Integer.valueOf(res.getString(res.getColumnIndex(TASK_PRIORITY))),
                    res.getString(res.getColumnIndex(TASK_DESCRIPTION)),
                    res.getString(res.getColumnIndex(TASK_COMPLETE)),
                    res.getString(res.getColumnIndex(TASK_REMINDER)),
                    res.getString(res.getColumnIndex(TASK_REMINDER_DTTM))
            );
            array_list.add(task_temp);
            res.moveToNext();
        }
        return array_list;
    }

    public int mark_task_done(int task_id)
    {
        int rows_updated = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(TASK_COMPLETE, "yes");
            rows_updated = db.update(TASK_TABLE, contentValues, "id = ? ", new String[] { Integer.toString(task_id) } );
        }
        finally {
            db.close();
        }
        return rows_updated;
    }

    public  int unmark_task_done(int task_id)
    {
        int rows_updated = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(TASK_COMPLETE, "no");
            rows_updated = db.update(TASK_TABLE, contentValues, "id = ? ", new String[] { Integer.toString(task_id) } );
        }
        finally {
            db.close();
        }
        return rows_updated;
    }
}
