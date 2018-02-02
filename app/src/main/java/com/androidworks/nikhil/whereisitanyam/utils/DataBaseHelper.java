package com.androidworks.nikhil.whereisitanyam.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.androidworks.nikhil.whereisitanyam.TimeZoneItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by nikhilkumar_s on 3/15/2017.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_PATH = "/data/data/com.androidworks.nikhil.whereisitanyam/databases/";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "timezones.db";
    private final Context myContext;
    private SQLiteDatabase myDataBase;
    //public static final int DATABASE_VERSION_old = 1;

    //Constructor
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            this.getReadableDatabase();
            try {
                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = myContext.getFilesDir().getPath() + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException {
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    //delete database
    public void db_delete() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase() throws SQLException {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
            try {
                createDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public ArrayList<TimeZoneItem> getTimes() {

        ArrayList<TimeZoneItem> list = new ArrayList<>();
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM TIMEZONES ORDER BY City ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeZoneItem timeZone = new TimeZoneItem();
            timeZone.setCity(cursor.getString(3));
            timeZone.setCountry(cursor.getString(2));
            timeZone.setTimezone(cursor.getString(4));
            timeZone.setTimezoneID(cursor.getString(5));
            timeZone.setCountryID(cursor.getString(1));
            //Log.d("nikhil", "Country ID: " + cursor.getString(1));
            cursor.moveToNext();
            list.add(timeZone);

        }
        cursor.close();
        closeDataBase();
       // Log.d("nikhil", list.toString());
        return list;
    }
}
