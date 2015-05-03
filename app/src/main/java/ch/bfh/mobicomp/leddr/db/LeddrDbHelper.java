package ch.bfh.mobicomp.leddr.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zimmerma on 01/05/15.
 */

public class LeddrDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "leddr.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String TERMINATOR = ";";

    // Device queries
    private static final String SQL_CREATE_DEVICES =
            "CREATE TABLE " + LeddrContract.DeviceEntry.TABLE_NAME + " (" +
                    LeddrContract.DeviceEntry._ID + " INTEGER PRIMARY KEY," +
                    LeddrContract.DeviceEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    LeddrContract.DeviceEntry.COLUMN_NAME_DEVICE_ID + TEXT_TYPE + COMMA_SEP +
                    LeddrContract.DeviceEntry.COLUMN_NAME_USER_ID + TEXT_TYPE +
            " )";

    // User queries
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + LeddrContract.UserEntry.TABLE_NAME + " (" +
                    LeddrContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    LeddrContract.UserEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    LeddrContract.UserEntry.COLUMN_NAME_USER_ID + TEXT_TYPE +
            " )";

    private static final String SQL_CREATE_ENTRIES =
            SQL_CREATE_DEVICES + TERMINATOR + SQL_CREATE_USERS + TERMINATOR;


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LeddrContract.DeviceEntry.TABLE_NAME + TERMINATOR +
            "DROP TABLE IF EXISTS " + LeddrContract.UserEntry.TABLE_NAME + TERMINATOR;

    public LeddrDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
