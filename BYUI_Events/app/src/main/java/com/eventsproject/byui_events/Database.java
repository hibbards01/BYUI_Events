package com.eventsproject.byui_events;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by SamuelHibbard on 12/5/14.
 */
public class Database extends SQLiteOpenHelper {
    /*
     * MEMBER VARIABLES
     */

    private final static String DATABASE_NAME = "BYUIEvents.db";
    private final static int VERSION = 1;
    private static Database database = null;
    //SQLiteDatabase sqLiteDatabase = null;

    //create table statment!
    private static final String SQL_CREATE_EVENT_TABLE =
            "CREATE TABLE IF NOT EXISTS event"
                    + "( event_id    INTEGER  NOT NULL"
                    + ", name        TEXT     NOT NULL"
                    + ", date        TEXT"
                    + ", start_time  TEXT"
                    + ", end_time    TEXT"
                    + ", description TEXT"
                    + ", category    TEXT"
                    + ", location    TEXT"
                    + ", picture     BLOB)";

    /*
     * MEMBER METHODS
     */

    /**
     * ONCREATE
     *  Create the database!
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table!
        db.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    /**
     * ONUPGRADE
     *  Upgrade the database!
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * CONSTRUCTOR
     * @param context
     */
    private Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * NEWINSTANCE
     *  Create a new instance!
     * @param context
     * @return
     */
    public static Database newInstance(Context context) {
        //see if it is null!
        if (database == null) {
            //create the database!
            database = new Database(context);
        }

        return database;
    }

    /**
     * GETINSTANCE
     *  Grab the class!
     * @return
     */
    public static Database getInstance() {
        return database;
    }

    /**
     * INSERTEVENT
     *  Add an event to the table!
     * @param textDate
     */
    public void insertEvent(String [] textDate, byte [] pic) {
        Log.d("SQL: ", "Adding to database!");
        //Log.d("SQL:Image", Integer.toString(pic.length));
        //grab the database!
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //now put the values into the table!
        values.put("event_id", textDate[0]);
        values.put("name", textDate[1]);
        values.put("date", textDate[2]);
        values.put("start_time", textDate[3]);
        values.put("end_time", textDate[4]);
        values.put("description", textDate[5]);
        values.put("category", textDate[6]);
        values.put("location", textDate[7]);
        values.put("picture", pic);

        //now insert it!
        db.insert("event", null, values);

        //remember to close it!
        db.close();
    }

    /**
     * SELECTEVENTS
     *  Select the events based on the day!
     * @param date
     * @param header
     * @param childs
     */
    public void selectEvents(String date, List<String> header, Map<String, String> childs,
                             List<byte[]> images) {
        //grab the database!
        SQLiteDatabase db = this.getReadableDatabase();

        //create select statment!
        Cursor cursor = db.rawQuery("SELECT * FROM event WHERE date = '"
                                    + date + "'", null);

        //now check to make sure there is something there!
        if (cursor != null && cursor.getCount() > 0) {
            //go to the first element in the list!
            cursor.moveToFirst();

            //and make some variables...
            String title;
            String child;
            int index = 0;

            //now loop thru all the events and grab them!
            for (int i = 0; i < cursor.getCount(); i++) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String dateText = cursor.getString(cursor.getColumnIndex("date"));
                String start_time = cursor.getString(cursor.getColumnIndex("start_time"));
                String end_time = cursor.getString(cursor.getColumnIndex("end_time"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                byte [] image = cursor.getBlob(cursor.getColumnIndex("picture"));

                //now combine them!
                title = name + "\n"
                        + timeFormat(start_time) + "-" + timeFormat(end_time);

                child = "Location: " + location + "\n" + category + "\n\n" + description + "\n";

                //now insert them into the lists and map!
                header.add(index, title);
                childs.put(title, child);
                images.add(index++, image);

                //now move forward by one..
                cursor.moveToNext();
            }
        } else {
            //display that no events are happening on this date!
            header.add(0, "No events today");
        }

        //remember to close it!
        cursor.close();
        db.close();
    }

    /**
     * TIMEFORMAT
     *  Change the format for the time!
     * @param textTime
     * @return
     */
    public String timeFormat(String textTime) {
        //make a new time!
        String[] splitTime = textTime.split(":");
        String time = splitTime[0] + splitTime[1];

        try {
            Date date = new SimpleDateFormat("hhmm").parse(time);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            time = sdf.format(date);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }

        return time;
    }

    public String formatDate(String dateText) {
        //split the date!

        //change the format the DATETEXT
        String date = null;

        return date;
    }

    /**
     * DELETEEVENTS
     *  Delete from the table!
     */
    public void deleteEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("event", null, null);
        db.close();
    }
}
