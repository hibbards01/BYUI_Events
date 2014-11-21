package com.eventsproject.byui_events;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.sql.Blob;

/**
 * Created by SamuelHibbard on 11/19/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //only want one instance of database!
    private DatabaseHelper db = null;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BYUI_Events.db";

    //for the table
    private static abstract class DatabaseEvent implements BaseColumns {
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PICTURE = "picture";
        public static final String COLUMN_EVENT_ID = "event_id";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
    }

    //table names
    private static final String TABLE_EVENT_NAME = "event";
    private static final String TABLE_COMMON_LOOKUP_NAME = "common_lookup";
    private static final String TABLE_FILTER_NAME = "filter";

    //create tables
    private static final String SQL_CREATE_EVENT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT_NAME
            + "( event_id    INTEGER  PRIMARY KEY"
            + ", name        TEXT"
            + ", date        TEXT"
            + ", start_time  TEXT"
            + ", end_time    TEXT"
            + ", description TEXT     NULL"
            + ", picture     BLOB     NULL)";

    private static final String SQL_CREATE_COMMON_LOOKUP_TABLE = "";
    private static final String SQL_CREATE_FILTER_TABLE = "";

    //delete tables
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS";

    /**
     * CONSTRUCTOR
     * @param context
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * NEWINSTANCE
     *  Create the class! Only create one instance!
     * @param context
     */
    public void newInstance(Context context) {
        if (db != null)
            db = new DatabaseHelper(context);
        else
            Log.d("ERROR: ", "Can only create one instance of this class");
    }

    /**
     * GETINSTANCE
     *  Grab the database!
     * @return
     */
    public DatabaseHelper getInstance() {
        return db;
    }

    /**
     * ONCREATE
     *  Create teh table.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    /**
     * ONUPGRADE
     *  Upgrade the table.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete the tables!
        db.execSQL(SQL_DELETE_TABLE + TABLE_EVENT_NAME);
        //now create them!
        onCreate(db);
    }

    /**
     * INSERTINTOTABLES
     *  Insert the data into the database!
     * @return
     */
    public boolean insertIntoTables() {
        //get the database!
        SQLiteDatabase db = this.getWritableDatabase();

        return true;
    }

    /**
     * GETDATA
     *  Grab the data
     * @return
     */
    public Cursor getData() {
        //grab the database!
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = null;

        return data;
    }
}
