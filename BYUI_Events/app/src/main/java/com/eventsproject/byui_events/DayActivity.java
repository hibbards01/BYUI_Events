package com.eventsproject.byui_events;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DayActivity extends Activity {

    /*
     * MEMBER VARIABLES
     */
    private List<String> headerList = new ArrayList<String>();
    private HashMap<String, String> childList = new HashMap<String, String>();
    private List<byte[]> images = new ArrayList<byte[]>();
    private ExpandableListViewAdapter listAdapter;
    private ExpandableListView expListView;
    private TextView dateView;
    private Database database = Database.getInstance();

    /*
     * MEMBER METHODS
     */

    /**
     * ONCREATE
     *  Create the list and display it!
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Date date = new Date();
        expListView = (ExpandableListView) findViewById(R.id.dayList);
        dateView = (TextView) findViewById(R.id.dayDate);

        //grab the date!
        String textDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        //now grab from the database!
        database.selectEvents(textDate, headerList, childList, images);

        //and grab the date so it can be at the title!
        textDate = dateFormat(textDate);
        dateView.setText(textDate);

        //now to put it on the screen!
        listAdapter = new ExpandableListViewAdapter(this, headerList, childList, images);

        //now set it to the screen!
        expListView.setAdapter(listAdapter);
    }

    /**
     * DATEFORMAT
     *  Change the format of the date!
     * @param textDate
     * @return
     */
    private String dateFormat(String textDate) {
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

    @Override
    protected void onStart() {
        super.onStart();
   }


//    @Override
//    public void onListItemClick(ListView list, View v, int position, long id) {
//
//    }
}

