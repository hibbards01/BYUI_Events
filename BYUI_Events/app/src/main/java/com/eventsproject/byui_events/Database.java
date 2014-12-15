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
 * Used to access the byui_events database and perform didn't MySQL operations on it.
 * Created by SamuelHibbard on 12/5/14.
 */
public class Database extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "BYUIEvents.db";
    private final static int VERSION = 1;
    private static Database database = null;

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

    private static final String SQL_CREATE_MY_EVENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS my_events"
                    + "( event_id    INTEGER  NOT NULL"
                    + ", name        TEXT     NOT NULL"
                    + ", date        TEXT"
                    + ", start_time  TEXT"
                    + ", end_time    TEXT"
                    + ", description TEXT"
                    + ", category    TEXT"
                    + ", location    TEXT"
                    + ", picture     BLOB)";

    /**
     *  Creates the database!
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table!
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_MY_EVENTS_TABLE);
    }

    /**
     *  Upgrades the database!
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Constructs a Database object
     * @param context
     */
    private Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     *  Creates a new instance and returns it!
     * @param context
     * @return
     */
    public static Database newInstance(Context context) {
        // See if it is null!
        if (database == null) {
            // Create the database!
            database = new Database(context);
        }

        return database;
    }

    /**
     * Gets the instance of the Database
     * @return
     */
    public static Database getInstance() {
        return database;
    }

    /**
     *  Insert the event into the myevents table!
     * @param header
     */
    public void insertMyEvents(String header) {
        // Grab the read database!
        SQLiteDatabase read = this.getReadableDatabase();

        // Now grab the right things to select the data!
        String [] splitHeader = header.split("~");
        String event_id = splitHeader[0];

        // Grab the event!
        Cursor cursor = read.rawQuery("SELECT * FROM event WHERE event_id = '"
                                     + event_id + "'", null);
        Log.d("Selecting event", event_id);

        // Grab the data... should be able to...
        if (cursor != null && cursor.getCount() > 0) {
            //go to the first element in the list!
            cursor.moveToFirst();

            // Grab everything!
            String eventId = cursor.getString(cursor.getColumnIndex("event_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String dateText = cursor.getString(cursor.getColumnIndex("date"));
            String start_time = cursor.getString(cursor.getColumnIndex("start_time"));
            String end_time = cursor.getString(cursor.getColumnIndex("end_time"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            String location = cursor.getString(cursor.getColumnIndex("location"));
            byte [] image = cursor.getBlob(cursor.getColumnIndex("picture"));

            // Now insert into the database!
            SQLiteDatabase write = this.getWritableDatabase();

            // Put the values into the content!
            ContentValues values = new ContentValues();
            values.put("event_id", eventId);
            values.put("name", name);
            values.put("date", dateText);
            values.put("start_time", start_time);
            values.put("end_time", end_time);
            values.put("description", description);
            values.put("category", category);
            values.put("location", location);
            values.put("picture", image);

            // Now insert it!
            Log.d("Inserting event", eventId);
            write.insert("my_events", null, values);
            write.close();
        }

        read.close();
    }

    /**
     *  This will select all the events from My_Events table.
     * @param header
     * @param childs
     * @param images
     * @param dateList
     */
    public void selectAllMy_Events(List<String> header, Map<String, String> childs,
                                   List<byte[]> images, Map<String, String[]> dateList) {
        // Now grab all the events!
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM my_events",null);
        Log.d("Selecting events", "All");

        gatherDataFromCursor(cursor, header, childs, images, dateList);
/*        // Now check to make sure there is something there!
        if (cursor != null && cursor.getCount() > 0) {
            // Go to the first element in the list!
            cursor.moveToFirst();

            // And make some variables...
            String child;
            int index = 0;

            // Now loop through all the events and grab them!
            for (int i = 0; i < cursor.getCount(); i++) {
                String event_id = cursor.getString(cursor.getColumnIndex("event_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String dateText = cursor.getString(cursor.getColumnIndex("date"));
                String start_time = cursor.getString(cursor.getColumnIndex("start_time"));
                String end_time = cursor.getString(cursor.getColumnIndex("end_time"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                byte [] image = cursor.getBlob(cursor.getColumnIndex("picture"));

                // Now combine them!
                String [] date = {dateText, (timeFormat(start_time) + "-" + timeFormat(end_time))};

                // This variable is only to make sure the maps have a unique key.
                String event = event_id + "~" + name;

                child = "Location: " + location + "\n" + category + "\n\n" + description + "\n";

                // Now insert them into the lists and map!
                header.add(index, event);
                childs.put(event, child);
                images.add(index++, image);
                dateList.put(event, date);

                // Now move forward by one..
                cursor.moveToNext();
            }
        } else {
            // Display that no events are happening on this date!
            Log.d("DEBUG1: ", "No events saved!");
        }

        // Remember to close it!
        cursor.close();*/
        db.close();
    }

    /**
     *  Add an event to the table!
     * @param textDate
     * @param pic
     */
    public void insertEvent(String [] textDate, byte [] pic) {
        Log.d("SQL: ", "Adding to database!");

        // Grab the database!
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Now put the values into the table!
        values.put("event_id", textDate[0]);
        values.put("name", textDate[1]);
        values.put("date", textDate[2]);
        values.put("start_time", textDate[3]);
        values.put("end_time", textDate[4]);
        values.put("description", textDate[5]);
        values.put("category", textDate[6]);
        values.put("location", textDate[7]);
        values.put("picture", pic);

        // Now insert it!
        db.insert("event", null, values);

        // Remember to close it!
        db.close();
    }

    /**
     *  Select the events based on the day!
     * @param startDate
     * @param endDate
     * @param header
     * @param childs
     * @param images
     * @param dateList
     */
    public void selectEvents(String startDate, String endDate, List<String> header, Map<String, String> childs,
                             List<byte[]> images, Map<String, String[]> dateList) {
        // Grab the database!
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        // Create select statement! Which one we want depends if startDate and endDate are the same or not.
        if (startDate.equals(endDate)) {
            cursor = db.rawQuery("SELECT * FROM event WHERE date = '"
                    + startDate + "'", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM event WHERE date BETWEEN '"
                                + startDate + "' AND '" + endDate
                                + "' ORDER BY date(date), start_time", null);
        }

        gatherDataFromCursor(cursor, header, childs, images, dateList);
/*        // Now check to make sure there is something there!
        if (cursor != null && cursor.getCount() > 0) {
            // Go to the first element in the list!
            cursor.moveToFirst();

            // And make some variables...
            String child;
            int index = 0;

            // Now loop through all the events and grab them!
            for (int i = 0; i < cursor.getCount(); i++) {
                String event_id = cursor.getString(cursor.getColumnIndex("event_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String dateText = cursor.getString(cursor.getColumnIndex("date"));
                String start_time = cursor.getString(cursor.getColumnIndex("start_time"));
                String end_time = cursor.getString(cursor.getColumnIndex("end_time"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                byte [] image = cursor.getBlob(cursor.getColumnIndex("picture"));

                //now combine them!
                String [] date = {dateText, (timeFormat(start_time) + "-" + timeFormat(end_time))};

                //this variable is only to make sure the maps have a unique key.
                String event = event_id + "~" + name;

                child = "Location: " + location + "\n" + category + "\n\n" + description + "\n";

                //now insert them into the lists and map!
                header.add(index, event);
                childs.put(event, child);
                images.add(index++, image);
                dateList.put(event, date);

                //now move forward by one..
                cursor.moveToNext();
            }
        } else {
            //display that no events are happening on this date!
            header.add(0, "No events today");
        }

        //remember to close it!
        cursor.close();*/
        db.close();
    }

    public void searchEvents(String queryTitle, List<String> header, Map<String, String> childs,
                             List<byte[]> images, Map<String, String[]> dateList) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        // Create select statement! Which one we want depends if startDate and endDate are the same or not.
        cursor = db.rawQuery("SELECT * FROM event WHERE name LIKE '%" + queryTitle + "%'", null);

        gatherDataFromCursor(cursor, header, childs, images, dateList);
    }

    /**
     * If the cursor isn't null then we need to get stuff from it and store it in the parameters provided.
     * @param cursor
     * @param header
     * @param childs
     * @param images
     * @param dateList
     */
    private void gatherDataFromCursor(Cursor cursor, List<String> header, Map<String, String> childs,
                                      List<byte[]> images, Map<String, String[]> dateList) {
        // Now check to make sure there is something there!
        if (cursor != null && cursor.getCount() > 0) {
            // Go to the first element in the list!
            cursor.moveToFirst();

            // And make some variables...
            String child;
            int index = 0;

            // Now loop through all the events and grab them!
            for (int i = 0; i < cursor.getCount(); i++) {
                String event_id = cursor.getString(cursor.getColumnIndex("event_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String dateText = cursor.getString(cursor.getColumnIndex("date"));
                String start_time = cursor.getString(cursor.getColumnIndex("start_time"));
                String end_time = cursor.getString(cursor.getColumnIndex("end_time"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                byte [] image = cursor.getBlob(cursor.getColumnIndex("picture"));

                // Now combine them!
                String [] date = {dateText, (timeFormat(start_time) + "-" + timeFormat(end_time))};

                // This variable is only to make sure the maps have a unique key.
                String event = event_id + "~" + name;

                child = "Location: " + location + "\n" + category + "\n\n" + description + "\n";

                // Now insert them into the lists and map!
                header.add(index, event);
                childs.put(event, child);
                images.add(index++, image);
                dateList.put(event, date);

                // Now move forward by one..
                cursor.moveToNext();
            }
        } else {
            // Display that no events are happening on this date!
            header.add(0, "No events today");
        }

        // Remember to close it!
        cursor.close();
    }

    /**
     *  Change the format for the time!
     * @param textTime
     * @return
     */
    public String timeFormat(String textTime) {
        // Make a new time!
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
        db.delete("my_events", null, null);
        db.close();
    }
}
