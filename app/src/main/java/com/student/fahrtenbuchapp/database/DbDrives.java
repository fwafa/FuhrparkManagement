package com.student.fahrtenbuchapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Date;

/**
 * Created by Mojdeh.aaz on 29.11.2016.
 */

public
class DbDrives extends SQLiteOpenHelper{
    public static final String TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "drives.db";
    public static final int DB_VERSION = 1;

    public static final String DRIVES_TABLE = "drives";
    public static final String COLUMN_DRIVEID = "id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_CAR = "car";
    public static final String COLUMN_STARTDATE = "startdate";
    public static final String COLUMN_ENDDATE = "enddate";
    public static final String COLUMN_STARTCOORD = "startcoord";
    public static final String COLUMN_ENDCOORD = "endcoord";
    public static final String COLUMN_STARTADDRESS = "startaddress";
    public static final String COLUMN_ENDADDRESS = "endaddress";
    public static final String COLUMN_STARTPOI = "startpoi";
    public static final String COLUMN_ENDPOI = "endpoi";
    public static final String COLUMN_STARTMILEAGE = "0";
    public static final String COLUMN_ENDMILEAGE = "0";
    public static final String COLUMN_USEDKWH = "0";


    public static final String CREATE_TABLE_DRIVES = "CREATE TABLE " + DRIVES_TABLE + "("
            + COLUMN_DRIVEID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER + " TEXT," + COLUMN_CAR + " TEXT,"
            + COLUMN_STARTDATE + " TEXT," + COLUMN_ENDDATE + " TEXT,"
            + COLUMN_STARTCOORD + " TEXT," + COLUMN_ENDCOORD + " TEXT,"
            + COLUMN_STARTADDRESS + " TEXT," + COLUMN_ENDADDRESS + " TEXT,"
            + COLUMN_STARTPOI + " TEXT," + COLUMN_ENDPOI + " TEXT,"
            + COLUMN_STARTMILEAGE + " TEXT," + COLUMN_ENDMILEAGE + " TEXT,"
            + COLUMN_USEDKWH + " TEXT,";

    public DbDrives(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DRIVES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DRIVES_TABLE);
        onCreate(db);
    }

    public void adddrive(String user, String car, Date startdate, Date enddate,
                       String startcoord, String endcoord, address startaddress, address endaddress,
                        String startpoi, String endpoi, int startmileage, int endmileage, int usedkwh)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER, user);
        values.put(COLUMN_CAR, car);
        values.put(COLUMN_STARTDATE, startdate.toString());
        values.put(COLUMN_ENDDATE, enddate.toString());
        values.put(COLUMN_STARTCOORD, startcoord);
        values.put(COLUMN_ENDCOORD, endcoord);
        values.put(COLUMN_STARTADDRESS, startaddress.country);
        values.put(COLUMN_STARTADDRESS, startaddress.city);
        values.put(COLUMN_STARTADDRESS, startaddress.zip);
        values.put(COLUMN_STARTADDRESS, startaddress.street);
        values.put(COLUMN_ENDADDRESS, endaddress.country);
        values.put(COLUMN_ENDADDRESS, endaddress.city);
        values.put(COLUMN_ENDADDRESS, endaddress.zip);
        values.put(COLUMN_ENDADDRESS, endaddress.street);
        values.put(COLUMN_STARTPOI,startpoi);
        values.put(COLUMN_ENDPOI, endpoi);
        values.put(COLUMN_STARTMILEAGE, startmileage);
        values.put(COLUMN_ENDMILEAGE, endmileage);
        values.put(COLUMN_USEDKWH, usedkwh);

        long id = db.insert(DRIVES_TABLE, null, values);
        db.close();
        Log.d(TAG, "user inserted " + id);
    }

    public boolean getDrive(String username, String car, Date startdate){
        String selectQuery = "select * from  " + DRIVES_TABLE + " where " +
                COLUMN_USER + " = " + "'"+username+"'" + " and " + COLUMN_CAR + " = " + "'"+car+"'"
                + " and " + COLUMN_STARTDATE + " = " + "'"+startdate+"'";

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

    public class address{
        String country,city,zip,street;
    }
}
