package com.eventsproject.byui_events;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.ListView;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SamuelHibbard on 11/19/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //only want one instance of database!
    private static DatabaseHelper db = null;
    private static Context mainContext = null;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BYUI_Events.db";

    //for the table
    private static abstract class DBEvent implements BaseColumns {
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PICTURE = "picture";
        public static final String COLUMN_EVENT_ID = "event_id";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_NAME = "name";
        public static final String [] ALL_COLUMN_NAMES = {
                COLUMN_EVENT_ID, COLUMN_NAME, COLUMN_DATE,
                COLUMN_START_TIME, COLUMN_END_TIME, COLUMN_DESCRIPTION,
                COLUMN_CATEGORY, COLUMN_LOCATION
        };
    }

    //table names
    private static final String TABLE_EVENT_NAME = "event";
    private static final String TABLE_SAVED_EVENTS = "saved_events";
    private static final String TABLE_COMMON_LOOKUP_NAME = "common_lookup";
    private static final String TABLE_FILTER_NAME = "filter";

    //create tables
    private static final String SQL_CREATE_EVENT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT_NAME
            + "( event_id    INTEGER  PRIMARY KEY NOT NULL"
            + ", name        TEXT     NOT NULL"
            + ", date        TEXT"
            + ", start_time  TEXT"
            + ", end_time    TEXT"
            + ", description TEXT"
            + ", category    TEXT"
            + ", location    TEXT)";

    private static final String SQL_CREATE_SAVED_EVENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SAVED_EVENTS
            + "( event_id    INTEGER  PRIMARY KEY NOT NULL"
            + ", name        TEXT     NOT NULL"
            + ", date        TEXT"
            + ", start_time  TEXT"
            + ", end_time    TEXT"
            + ", description TEXT"
            + ", category    TEXT"
            + ", location    TEXT)";

    private static final String SQL_CREATE_COMMON_LOOKUP_TABLE = "";
    private static final String SQL_CREATE_FILTER_TABLE = "";

    //delete tables
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS";

    /**
     * CONSTRUCTOR
     * @param context
     */
    private DatabaseHelper(Context context) {
        //create the database!
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mainContext = context;
    }

    /**
     * NEWINSTANCE
     *  Create the class! Only create one instance!
     * @param context
     */
    public static DatabaseHelper newInstance(Context context) {

        if (db == null) {
            //context.deleteDatabase(DATABASE_NAME);
            db = new DatabaseHelper(context);
        }
        return db;
    }

    /**
     * DATABASEHELPER
     *  Grab the database!
     * @return
     */
    public static DatabaseHelper getInstance() {
        Log.d("SQL: ", "Getting database");
        return db;
    }

    /**
     * ONCREATE
     *  Create the table.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        Log.d("SQLCreated:", "Created table!");
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
    public void addEvent(String [] data) {
        Log.d("SQL:", "Adding to database!");
        //get the database!
        SQLiteDatabase db = this.getWritableDatabase();

        //create a map of values!
        ContentValues values = new ContentValues();

        //now grab the values!
        //values.put(DBEvent.COLUMN_EVENT_ID, Integer.parseInt(data[0]));
        values.put(DBEvent.COLUMN_NAME, data[1]);
        values.put(DBEvent.COLUMN_DATE, data[2]);
        values.put(DBEvent.COLUMN_START_TIME, data[3]);
        values.put(DBEvent.COLUMN_END_TIME, data[4]);
        values.put(DBEvent.COLUMN_DESCRIPTION, data[5]);
        values.put(DBEvent.COLUMN_CATEGORY, data[6]);
        values.put(DBEvent.COLUMN_LOCATION, data[7]);

        //now insert them!
        db.insert(TABLE_EVENT_NAME, null, values);

        db.close();

        return;
    }

    /**
     * GETEVENTSBETWEENDATES
     *  Grab the data
     * @param start_date
     * @param end_date
     */
    public void getEventsBetweenDates(String start_date, String end_date, List<String> titleList, Map<String, String> childList) {
        //grab the database!
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM event WHERE date "
                                    + "BETWEEN '" + start_date + "' AND '"
                                    + end_date + "'", null);

        //now grab from it!
        grabFromCursor(cursor, titleList, childList);

        db.close();

        return;
    }

    /**
     * GRABFROMCURSOR
     *  Grab the events!
     * @param cursor
     * @param titleList
     * @param childList
     */
    public void grabFromCursor(Cursor cursor, List<String> titleList, Map<String, String> childList) {
        //create some variables!
        String title;
        String child;
        int index = 0;

        //make sure that we found something!
        if (cursor != null && cursor.getCount() > 0) {
            Log.d("SQL_getEvent: ", "Found some data!");
            cursor.moveToFirst();

            //now loop thru them all!
            for (int i = 0; i < cursor.getCount(); i++) {
                //create some strings and grab it!
                String event_id = cursor.getString(cursor.getColumnIndex(DBEvent.COLUMN_EVENT_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBEvent.COLUMN_NAME));
                String grabDate = cursor.getString(cursor.getColumnIndex(DBEvent.COLUMN_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(DBEvent.COLUMN_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndex(DBEvent.COLUMN_END_TIME));
                String description = cursor.getString(cursor.getColumnIndex(DBEvent.COLUMN_DESCRIPTION));
                String category = cursor.getString(cursor.getColumnIndex(DBEvent.COLUMN_CATEGORY));
                String location = cursor.getString(cursor.getColumnIndex(DBEvent.COLUMN_LOCATION));

                //phew! now the create a new event!
//                Event event = new Event(event_id, name, grabDate, startTime, endTime, description,
//                                        category, location);

                //grab title and child!
                title = name + "\n\n" + dateFormat(grabDate) + " " + startTime + "-" + endTime;
                child = "Location: " +  location + "\n" + category + "\n\n" + description;

                //now add to the TITLELIST and CHILDLIST!
                titleList.add(index++, title);
                childList.put(title, child);

                //now move to the next data!
                cursor.moveToNext();

                Log.d("SQL_getEvent: ", event_id + " " + name + " " + grabDate + " " + location);
            }
        }
    }

    /**
     * GETEVENT
     *  Grab the event on the date!
     * @param date
     */
    public void getEvent(String date, List<String> titleList, Map<String, String> childList) {
        Log.d("SQL_getEvent: ", "Grabbing the event!");
        //now grab from the datebase!
        SQLiteDatabase db = this.getReadableDatabase();

        //now select it!
        Cursor cursor = db.rawQuery("SELECT * FROM event WHERE date = '"
                                    + date + "'", null);

        //now grab from the cursor!
        grabFromCursor(cursor, titleList, childList);

        db.close();
    }

    /**
     * DATEFORMAT
     *  Change the format of the date!
     * @param textDate
     * @return
     */
    public String dateFormat(String textDate) {
        //create the variables!
        String date;
        String [] splitDate = textDate.split("-");
        String [] month = {
                "none", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        date = month[Integer.parseInt(splitDate[1])] + " " + splitDate[2] + " " + splitDate[0];

        return date;
    }

    /**
     * TIMEFORMAT
     *  Change the format for the time!
     * @param textTime
     * @return
     */
    public String timeFormat(String textTime) {


        return textTime;
    }

    public void deleteFromEvent() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENT_NAME, null, null);
    }

    public void deleteDB() {
        mainContext.deleteDatabase(DATABASE_NAME);
    }
}
