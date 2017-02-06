package io.github.zhaeong.worktracker.TaskConstructs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by owen_ on 2017-01-24.
 * Custom implementation of database helper
 */

public class CustomDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TaskItems.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TASKS_TABLE_NAME = "TASKS";
    public static final String TASKS_COL_ID = "_id";
    public static final String TASKS_COL_NAME = "TaskName";
    public static final String TASKS_COL_DESC = "TaskDesc";
    public static final String TASKS_CREATION_DATETIME = "TaskCreDateTime";
    public static final String TASKS_START_DATETIME = "TaskStartDateTime";
    public static final String TASKS_END_DATETIME = "TaskEndDateTime";
    public static final String TASKS_ELAPSED = "TaskElapsed";
    public static final String TASK_IS_ACTIVE = "isActive";

    public static final String TABLE_CREATE_STATEMENT =
        "CREATE TABLE " +
        TASKS_TABLE_NAME +
        "( " + TASKS_COL_ID + " INTEGER PRIMARY KEY, " +
        TASKS_COL_NAME +
        " TEXT, " +
        TASKS_COL_DESC +
        " TEXT, " +
        TASKS_CREATION_DATETIME +
        " INTEGER, "+
        TASKS_START_DATETIME +
        " INTEGER, "+
        TASKS_END_DATETIME +
        " INTEGER, "+
        TASKS_ELAPSED +
        " INTEGER, "+
        TASK_IS_ACTIVE +
        " INTEGER DEFAULT 0 "+
        " )";

    public CustomDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_STATEMENT);
        Log.i("DatabaseHelper", "executed onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TASKS_TABLE_NAME);
        Log.i("DatabaseHelper", "executed onUpgrade");
        onCreate(db);
    }

    public boolean addTask (String taskName, String taskDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Date curDate = new Date();
        Long curTime = curDate.getTime();

        contentValues.put(TASKS_COL_NAME, taskName);
        contentValues.put(TASKS_COL_DESC, taskDesc);
        contentValues.put(TASKS_CREATION_DATETIME, curTime);
        long pk = db.insert(TASKS_TABLE_NAME, null, contentValues);
        Log.i("DatabaseHelper", "executed addTask, TaskID:" + pk + "TaskName:" + taskName);
        return true;
    }

    public boolean updateTask (Long task_id, String taskName, String taskDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_COL_NAME, taskName);
        contentValues.put(TASKS_COL_DESC, taskDesc);
        db.update(TASKS_TABLE_NAME, contentValues, "_id  = ? ", new String[] { Long.toString(task_id) } );
        Log.i("DatabaseHelper", "executed updateTask, TaskID: " + task_id + " TaskName: " + taskName);
        return true;
    }

    public Cursor getTask(Long task_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from " + TASKS_TABLE_NAME + " where _id = " + task_id.toString();
        Cursor result = db.rawQuery( sqlQuery, null );
        if(result.getCount() > 0) {
            result.moveToFirst();
        }
        return result;
    }

    public Cursor getActiveTask()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from " + TASKS_TABLE_NAME + " where isActive = " + 1;
        Cursor result = db.rawQuery( sqlQuery, null );
        if(result.getCount() > 0) {
            result.moveToFirst();
        }
        return result;

    }

    public Integer deleteTask (Long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASKS_TABLE_NAME,
                "_id = ? ",
                new String[] { Long.toString(task_id) });
    }

    public Cursor getAllTasks()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery( "select * from " + TASKS_TABLE_NAME, null );
    }

    public boolean TaskActivation(Long task_id, int isActive)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Date curDate = new Date();
        Long curTime = curDate.getTime();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_IS_ACTIVE, isActive);


        if(isActive == 1) //add the start time
        {
            deactivateAllOtherTasks(task_id);
            contentValues.put(TASKS_START_DATETIME, curTime);
        }
        else if (isActive == 0) //add the end time and increment time elapsed accordingly
        {

            long lStartTime = Long.parseLong(getTaskInfo(task_id, TASKS_START_DATETIME));
            long alreadyElapsedTime = 0;
            if(getTaskInfo(task_id, TASKS_ELAPSED) != null)
            {
                alreadyElapsedTime = Long.parseLong(getTaskInfo(task_id, TASKS_ELAPSED));
            }

            long elapsedTime = (curTime - lStartTime) + alreadyElapsedTime;
            contentValues.put(TASKS_END_DATETIME, curTime);
            contentValues.put(TASKS_ELAPSED, elapsedTime);
            Log.i("DatabaseHelper", "starttime: " +lStartTime + "endtime:" + curTime + "elapsedtime: " + elapsedTime);
        }

        db.update(TASKS_TABLE_NAME, contentValues, "_id  = ? ", new String[] { Long.toString(task_id) } );
        Log.i("DatabaseHelper", "executed TaskActivation, TaskID: " + task_id + ", isActive:" + isActive);
        return true;

    }

    public String getTaskInfo(Long task_id, String tableColumn)
    {
        String resultInfo = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select " + tableColumn + " from " + TASKS_TABLE_NAME + " where _id = " + task_id;
        Cursor curResult = db.rawQuery( sqlQuery, null );
        if(curResult.getCount() > 0) {
            curResult.moveToFirst();
            resultInfo = curResult.getString(curResult.getColumnIndex(tableColumn));
        }
        curResult.close();
        return resultInfo;
    }

    public void deactivateAllOtherTasks(Long task_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_IS_ACTIVE, 0);
        db.update(TASKS_TABLE_NAME, contentValues, "_id  != ? ", new String[] { Long.toString(task_id) } );
    }

/*
    public ArrayList<TaskObject> getAlltasks()
    {
        ArrayList<TaskObject> TOlist = new ArrayList<TaskObject>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TASKS_TABLE_NAME, null );
        try {
            res.moveToFirst();

            while (!res.isAfterLast()) {

                String taskName = res.getString(res.getColumnIndex(TASKS_COL_NAME));
                String taskDesc = res.getString(res.getColumnIndex(TASKS_COL_DESC));

                TaskObject to = new TaskObject(taskName, taskDesc);
                TOlist.add(to);

                res.moveToNext();
            }
        }
        finally {
            res.close();
        }


        return TOlist;
    }
*/

}
