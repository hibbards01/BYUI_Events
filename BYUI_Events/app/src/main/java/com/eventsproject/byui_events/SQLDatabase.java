package com.eventsproject.byui_events;

import java.sql.*;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * SQLDATABASE
 * Created by SamuelHibbard on 11/18/14.
 */
public class SQLDatabase implements Runnable {
    //  Database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://174.27.90.72:3306/byui_events";

    //  Database credentials
    static final String USER = "sam";
    static final String PASS = "hibbard";

    //grab the database!
    private DatabaseHelper db = DatabaseHelper.getInstance();

    /**
     * GETDATA
     *  This will grab from the database!
     */
    public void run() {
        //create the variables!
        Connection conn = null;
        Statement stmt = null;
        //now to grab from the database!
        Log.d("SQL: ", "Entered into the getDATA()");
        try {
            Log.d("SQL: ", "Entered into TRY");
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER).newInstance();

            //STEP 3: Open a connection
            Log.d("SQL: ", "Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            Log.d("SQL: ","Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT event_id, name, description, date, start_time, end_time, category, location FROM event";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()) {
                //Retrieve by column name
                String [] data = new String[8];

                /****************************************************************
                 * NOTE these are in a certain order. If you change this then make
                 * sure to change the order in the DATABASEHELPER in the function ADDEVENT.
                 ****************************************************************/
                data[0] = rs.getString("event_id");
                data[1] = rs.getString("name");
                data[2] = rs.getString("date");
                data[3] = rs.getString("start_time");
                data[4] = rs.getString("end_time");
                data[5] = rs.getString("description");
                data[6] = rs.getString("category");
                data[7] = rs.getString("location");

                //now insert it into the table!
                db.addEvent(data);

                //Display values
                Log.d("SQL: ", "ID: " + data[0]);
                Log.d("SQL: ","Name: " + data[1] + "  date: " + data[2] + "  start_time: " + data[3] + "  end_time: " + data[4]);

                Log.d("SQL: ","description: " + data[5]);
            }

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException se){
            //Handle errors for JDBC
            Log.d("SQL: ", "SQLException");
            se.printStackTrace();
        } catch(Exception e){
            //Handle errors for Class.forName
            Log.d("SQL: ", "Exception");
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            Log.d("SQL: ", "Entered into FINALLY");
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        Log.d("SQL: ","Goodbye!");
    }
}
