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

public
class DbUsers extends SQLiteOpenHelper {
    public static final String TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "users.db";
    public static final int DB_VERSION = 1;

    public static final String USERS_TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASS = "password";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ROLE = "role";

    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USERS_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT," + COLUMN_PASS + " TEXT);"
            + COLUMN_FIRSTNAME + " TEXT," + COLUMN_LASTNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT," + COLUMN_PHONE + " TEXT,"
            + COLUMN_ROLE + " TEXT,";
//


    public DbUsers(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        onCreate(db);
    }

    public void addUser(String username, String password, String firstname, String lastname,
                        String email, String phone, String role)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASS, password);
        values.put(COLUMN_FIRSTNAME, firstname);
        values.put(COLUMN_LASTNAME, lastname);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_ROLE, role);

        long id = db.insert(USERS_TABLE, null, values);
        db.close();
        Log.d(TAG, "user inserted " + id);
    }

    public boolean getUser(String username, String pass){
        String selectQuery = "select * from  " + USERS_TABLE + " where " +
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
