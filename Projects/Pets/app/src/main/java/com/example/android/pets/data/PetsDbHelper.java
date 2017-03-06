package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.pets.data.PetsContract.PetsEntry;

/**
 * Created by Reddyz on 03-03-2017.
 */


public class PetsDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = "PetsDbHelper";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "shelter.db";

    public PetsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(LOG_TAG, "Constructor: "+ DATABASE_NAME + "  " + DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + PetsEntry.TABLE_NAME + " (" +
                        PetsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PetsEntry.COLUMN_PET_NAME + " TEXT NOT NULL, " +
                        PetsEntry.COLUMN_PET_BREED + " TEXT, " +
                        PetsEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, " +
                        PetsEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0)";

        Log.i(LOG_TAG, "SQL create: "+ SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PetsEntry.TABLE_NAME;
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        Log.i(LOG_TAG, "SQL Upgrade: "+ SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
