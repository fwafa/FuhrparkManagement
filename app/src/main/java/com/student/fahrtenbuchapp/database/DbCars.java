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
class DbCars extends SQLiteOpenHelper{
    public static final String TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "cars.db";
    public static final int DB_VERSION = 1;

    public static final String CARS_TABLE = "cars";
    public static final String COLUMN_VENDOR = "vendor";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_MILEAGE = "0";
    public static final String COLUMN_AVAILABLE = "false";
    public static final String COLUMN_BATTERY = "0";
    public static final String COLUMN_CHARGING = "false";
    public static final String COLUMN_CHARGEDKWH = "0";
    public static final String COLUMN_LICENSENUMBER = "licensenumber";

    public static final String CREATE_TABLE_CARS = "CREATE TABLE " + CARS_TABLE + "("
            + COLUMN_LICENSENUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_VENDOR + " TEXT," + COLUMN_MODEL + " TEXT,"
            + COLUMN_MILEAGE + " TEXT," + COLUMN_AVAILABLE + " TEXT,"
            + COLUMN_BATTERY + " TEXT," + COLUMN_CHARGING + " TEXT,"
            + COLUMN_CHARGEDKWH + " TEXT," + COLUMN_LICENSENUMBER + " TEXT,";

    public DbCars(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CARS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CARS_TABLE);
        onCreate(db);
    }

    public void addCar(String vendor, String model, int mileage, boolean available,
                        int battery, boolean charging, int chargedkwh, String licensenumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VENDOR, vendor);
        values.put(COLUMN_MODEL, model);
        values.put(COLUMN_MILEAGE, mileage);
        values.put(COLUMN_AVAILABLE, available);
        values.put(COLUMN_BATTERY, battery);
        values.put(COLUMN_CHARGING, charging);
        values.put(COLUMN_CHARGEDKWH, chargedkwh);
        values.put(COLUMN_LICENSENUMBER, licensenumber);

        long id = db.insert(CARS_TABLE, null, values);
        db.close();
        Log.d(TAG, "user inserted " + id);
    }

    public boolean getCar(String vendor, String licensenumber){
        String selectQuery = "select * from  " + CARS_TABLE + " where " +
                COLUMN_VENDOR + " = " + "'"+vendor+"'" + " and " + COLUMN_LICENSENUMBER + " = " + "'"+licensenumber+"'";

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
