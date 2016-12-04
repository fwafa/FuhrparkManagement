package com.student.fahrtenbuchapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mojdeh.aaz on 29.11.2016.
 */

public class DbLogin extends SQLiteOpenHelper {

    public static final String TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "driver_login.db";
    public static final int DB_VERSION = 1;

    public static final String LOGIN_TABLE = "login";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASS = "password";


    public static final String CREATE_TABLE_LOGIN = "CREATE TABLE " + LOGIN_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASS + " TEXT);";

    public DbLogin(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOGIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
        onCreate(db);
    }

    public void addLoginUser(String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASS, password);

        long id = db.insert(LOGIN_TABLE, null, values);
        db.close();
        Log.d(TAG, "user inserted " + id);
    }

    public boolean getLoginUser(String username, String pass){
        String selectQuery = "select * from  " + LOGIN_TABLE + " where " +
                COLUMN_USERNAME + " = " + "'"+username+"'" + " and " + COLUMN_PASS + " = " + "'"+pass+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
}
