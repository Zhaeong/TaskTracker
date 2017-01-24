package io.github.zhaeong.worktracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by owen_ on 2017-01-24.
 * Custom implementation of database helper
 */

public class CustomDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TaskItems.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CREATE_STATEMENT =
            "CREATE TABLE" +
            "TASKS" +
            "TaskId integer primary key, " +
            "TaskName TEXT, " +
            "TaskDesc TEXT";

    public CustomDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact (String taskName, String taskDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TaskName", taskName);
        contentValues.put("TaskDesc", taskDesc);
        db.insert("TASKS", null, contentValues);
        return true;
    }

}
