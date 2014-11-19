package com.eventsproject.byui_events;

import java.sql.*;
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
            sql = "SELECT event_id, name, description, date, start_time, end_time FROM event";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()) {
                //Retrieve by column name
                String name = rs.getString("name");
                String description = rs.getString("description");
                String date = rs.getString("date");
                String start_time = rs.getString("start_time");
                String end_time = rs.getString("end_time");

                //Display values
                //System.out.print("ID: " + id);
                //System.out.print(", Age: " + age);
                Log.d("SQL: ","Name: " + name + "  date: " + date + "  start_time: " + start_time + "  end_time: " + end_time);

                Log.d("SQL: ","description: " + description);
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
