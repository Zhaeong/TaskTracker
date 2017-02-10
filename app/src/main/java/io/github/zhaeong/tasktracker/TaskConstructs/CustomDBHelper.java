package io.github.zhaeong.tasktracker.TaskConstructs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by owen_ on 2017-01-24.
 * Custom implementation of database helper
 */

public class CustomDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TaskItems.db";
    public static final int DATABASE_VERSION = 1;


    //
    //Tasks table static variables
    //
    public static final String TASKS_TABLE_NAME = "TASKS";

    public static final String TASKS_COL_ID = "_id";
    public static final String TASKS_COL_NAME = "TaskName";
    public static final String TASKS_COL_DESC = "TaskDesc";
    public static final String TASKS_COL_DAYID = "TaskDayId";
    public static final String TASKS_CREATION_DATETIME = "TaskCreDateTime";
    public static final String TASKS_START_DATETIME = "TaskStartDateTime";
    public static final String TASKS_END_DATETIME = "TaskEndDateTime";
    public static final String TASKS_ELAPSED = "TaskElapsed";
    public static final String TASK_IS_ACTIVE = "isActive";
    public static final String TASK_IS_FINISHED = "isFinished";

    public static final String TABLE_CREATE_TASKS =
        "CREATE TABLE " +
        TASKS_TABLE_NAME +
        "( " + TASKS_COL_ID + " INTEGER PRIMARY KEY, " +
        TASKS_COL_NAME +
        " TEXT, " +
        TASKS_COL_DESC +
        " TEXT, " +
        TASKS_COL_DAYID +
        " INTEGER, " +
        TASKS_CREATION_DATETIME +
        " INTEGER, "+
        TASKS_START_DATETIME +
        " INTEGER, "+
        TASKS_END_DATETIME +
        " INTEGER, "+
        TASKS_ELAPSED +
        " INTEGER, "+
        TASK_IS_ACTIVE +
        " INTEGER DEFAULT 0, "+
        TASK_IS_FINISHED +
        " INTEGER DEFAULT 0 "+
        " )";

    //
    //Days table static variables
    //
    public static final String DAYS_TABLE_NAME = "DAYS";
    public static final String DAYS_COL_ID = "_id";
    public static final String DAYS_COL_NAME = "DayName";
    public static final String DAYS_WAKETIME = "DaysWakeTime";
    public static final String DAYS_SLEEPTIME = "DaysSleepTime";
    public static final String DAYS_IS_ACTIVE = "isActive";


    public static final String TABLE_CREATE_DAYS =
            "CREATE TABLE " +
            DAYS_TABLE_NAME +
            "( " + DAYS_COL_ID + " INTEGER PRIMARY KEY, " +
            DAYS_COL_NAME +
            " TEXT, " +
            DAYS_WAKETIME +
            " INTEGER, "+
            DAYS_SLEEPTIME +
            " INTEGER, "+
            DAYS_IS_ACTIVE +
            " INTEGER DEFAULT 0 " +
            " )";

    //
    //Methods Begin
    //

    public CustomDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_TASKS);
        db.execSQL(TABLE_CREATE_DAYS);
        Log.i("DatabaseHelper", "executed onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + DAYS_TABLE_NAME);
        Log.i("DatabaseHelper", "executed onUpgrade");
        onCreate(db);
    }

    //////////////////////////////////////////////////////////////////////////////////
    //
    //Tasks Database Functions
    //
    /////////////////////////////////////////////////////////////////////////////////

    public Cursor getAllItemsInTable(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery( "select * from " + tableName, null );
    }

    public Cursor getAllUnfinishedTasks()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery( "select * from " + TASKS_TABLE_NAME
                            + " Where " + TASK_IS_FINISHED + " = 0" , null );
    }

    public Cursor getAllTasksOfDay(long dayId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery( "select * from " + TASKS_TABLE_NAME
                + " Where " + TASKS_COL_DAYID + " = " + dayId , null );
    }

    public long addTask (String taskName, String taskDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Date curDate = new Date();
        Long curTime = curDate.getTime();

        Cursor curActiveDay = findActiveDay();
        if(curActiveDay.getCount() == 1)
        {
            curActiveDay.moveToFirst();
            long dayID = curActiveDay.getLong(curActiveDay.getColumnIndex(DAYS_COL_ID));
            contentValues.put(TASKS_COL_NAME, taskName);
            contentValues.put(TASKS_COL_DESC, taskDesc);
            contentValues.put(TASKS_COL_DAYID, dayID);
            contentValues.put(TASKS_CREATION_DATETIME, curTime);
            long pk = db.insert(TASKS_TABLE_NAME, null, contentValues);
            Log.i("DatabaseHelper", "executed addTask, TaskID:" + pk + "TaskName:" + taskName);
            return pk;
        }
        else
        {
            Log.e("DatabaseHelper", "Get active day error");
            return -1;
        }
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

    public Cursor getItemInTable(Long task_id, String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from " + tableName + " where _id = " + task_id.toString();
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

    public boolean TaskActivation(Long task_id, int isActive)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Date curDate = new Date();
        Long curTime = curDate.getTime();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_IS_ACTIVE, isActive);


        if(isActive == 1) //add the start time
        {
            deactivateCurActiveTask();
            contentValues.put(TASKS_START_DATETIME, curTime);
        }
        else if (isActive == 0) //add the end time and increment time elapsed accordingly
        {
            long lStartTime = 0;
            long alreadyElapsedTime = 0;
            long totalElapsedTime = 0;

            if(getTaskInfo(task_id, TASKS_START_DATETIME) != null) {
                lStartTime = Long.parseLong(getTaskInfo(task_id, TASKS_START_DATETIME));

                if (getTaskInfo(task_id, TASKS_ELAPSED) != null) {
                    alreadyElapsedTime = Long.parseLong(getTaskInfo(task_id, TASKS_ELAPSED));
                }

                totalElapsedTime = (curTime - lStartTime) + alreadyElapsedTime;
            }

            contentValues.put(TASKS_END_DATETIME, curTime);
            contentValues.put(TASKS_ELAPSED, totalElapsedTime);
            Log.i("DatabaseHelper", "starttime: " +lStartTime + "endtime:" + curTime + "elapsedtime: " + totalElapsedTime);
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
        else
        {
            Log.e("DatabaseHelper", "unable to getTaskInfo");
        }
        curResult.close();
        return resultInfo;
    }

    public void deactivateCurActiveTask() //Deactivates currently active task and sets the end time accordingly
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "select _id from " + TASKS_TABLE_NAME + " where isActive = 1";
        Cursor curResult = db.rawQuery( sqlQuery, null );
        if(curResult.getCount() == 1) {
            curResult.moveToFirst();
            Long taskId = curResult.getLong(curResult.getColumnIndex(TASKS_COL_ID));
            TaskActivation(taskId, 0);

        }
        else
        {
            Log.e("DatabaseHelper", "More than 1 active task at a time");
        }
        curResult.close();
    }

    public void finishAllTasksInDay(Long dayId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        deactivateCurActiveTask();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_IS_FINISHED, 1);
        db.update(TASKS_TABLE_NAME, contentValues, TASKS_COL_DAYID + " = ? ", new String[] { Long.toString(dayId) } );

    }
    //////////////////////////////////////////////////////////////////////////////////
    //
    //Days Database Functions
    //
    /////////////////////////////////////////////////////////////////////////////////

    public long createDay()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Date curDate = new Date();
        Long curTime = curDate.getTime();
        //String dayName = DateFormat.getDateInstance(DateFormat.SHORT, Locale.CANADA).format(curDate);
        SimpleDateFormat SDF = new SimpleDateFormat("MM/dd hh:mm");
        String dayName = SDF.format(curDate);

        contentValues.put(DAYS_COL_NAME, dayName);
        contentValues.put(DAYS_WAKETIME, curTime);
        contentValues.put(DAYS_IS_ACTIVE, 1);
        long pk = db.insert(DAYS_TABLE_NAME, null, contentValues);
        printAllItemsInTable(DAYS_TABLE_NAME);
        return pk;
    }

    public void endDay()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Date curDate = new Date();
        Long curTime = curDate.getTime();

        Cursor CurActiveDay = findActiveDay();
        if(CurActiveDay.getCount() == 1) {

            CurActiveDay.moveToFirst();
            long dayIdActive = CurActiveDay.getLong(CurActiveDay.getColumnIndex(DAYS_COL_ID));
            String dayName = CurActiveDay.getString(CurActiveDay.getColumnIndex(DAYS_COL_NAME));

            SimpleDateFormat SDF = new SimpleDateFormat("MM/dd hh:mm");
            dayName += " - " +  SDF.format(curDate);

            finishAllTasksInDay(dayIdActive);

            contentValues.put(DAYS_COL_NAME, dayName);
            contentValues.put(DAYS_SLEEPTIME, curTime);
            contentValues.put(DAYS_IS_ACTIVE, 0);
            db.update(DAYS_TABLE_NAME, contentValues, "_id  = ? ", new String[] { Long.toString(dayIdActive) } );

            CurActiveDay.close();
            printAllItemsInTable(DAYS_TABLE_NAME);
        }
        else
        {
            Log.e("DatabaseHelper", "more than one active day");
        }

    }

    public Cursor findActiveDay(){

        SQLiteDatabase db = this.getReadableDatabase();
        long task_id = 0;

        String sqlQuery = "select * from " + DAYS_TABLE_NAME + " where " + DAYS_IS_ACTIVE +" = " + 1;
        return db.rawQuery( sqlQuery, null );
    }

    public boolean isThereActiveDay()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from " + DAYS_TABLE_NAME + " where " + DAYS_IS_ACTIVE +" = " + 1;
        Cursor activeDay = db.rawQuery( sqlQuery, null );
        if(activeDay.getCount() == 1)
        {
            activeDay.close();
            return true;
        }
        activeDay.close();
        return false;
    }

    public void deleteDay(long dayId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DAYS_TABLE_NAME,
                "_id = ? ",
                new String[] { Long.toString(dayId) });
        db.delete(TASKS_TABLE_NAME,
                TASKS_COL_DAYID + "= ?",
                new String[] { Long.toString(dayId) });
    }


    //////////////////////////////////////////////////////////////////////////////////
    //
    //Debugging Functions
    //
    /////////////////////////////////////////////////////////////////////////////////
    public void printAllItemsInTable(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + tableName, null );
        try {
            res.moveToFirst();
            String loggerMSG = "";
            while (!res.isAfterLast()) {
                String loggerLine = "";
                for(int i = 0; i < res.getColumnCount(); i++)
                {
                    loggerLine += res.getString(i);
                    loggerLine += " ";
                }

                loggerLine += "\n";
                loggerMSG +=loggerLine;

                res.moveToNext();
            }
            Log.i("DatabaseHelper", loggerMSG);
        }
        finally {
            res.close();
        }

    }


}
