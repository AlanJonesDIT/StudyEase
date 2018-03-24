package com.alanhughjones.studyeasy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alanh on 24/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tasks.db";
    public static final String TABLE_NAME = "subject_table";
    public static final String SUBJECT_ID = "SUBJECT_ID";
    public static final String SUBJECT_NAME = "SUBJECT_NAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase(); // just for testing
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT_NAME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(db);
    }
}
