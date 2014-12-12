package com.eventsproject.byui_events;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyEventsActivity extends Activity {
    /*
     * MEMBER VARIABLES
     */
    private ExpandableListViewAdapter listAdapter = null;
    private ExpandableListView expListView;
    private TextView dateView;
    private Database database = Database.getInstance();
    //private GestureDetector gd;
    private Date date;

    /*
     * MEMBER METHODS
     */

    /**
     * ONCREATE
     *  Create the event!
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        //create a new gesture!
        //gd = new GestureDetector(this, this);

        //grab the date!
        date = new Date();
        expListView = (ExpandableListView) findViewById(R.id.myEventsList);
        dateView = (TextView) findViewById(R.id.myEventsDate);

        setAdapter();
    }

    /**
     * SETADAPTER
     */
    private void setAdapter() {
        //create the lists!
        List<String> headerList = new ArrayList<String>();
        Map<String, String> childList = new HashMap<String, String>();
        List<byte[]> images = new ArrayList<byte[]>();
        Map<String, String[]> dateList = new HashMap<String, String[]>();

        //now grab from the database!
        database.selectAllMy_Events(headerList, childList, images, dateList);

        //check the lists!
        if (headerList.size() == 0) {
            dateView.setText("No events saved");
        }

        //now to put it on the screen!
        if (listAdapter == null) {
            Log.d("My_Events: ", "create list adapter");
            listAdapter = new ExpandableListViewAdapter(this, headerList, childList, images, dateList, "MYEVENTS");
        } else {
            listAdapter.setLists(headerList, childList, images, dateList);
        }

        //now set it to the screen!
        expListView.setAdapter(listAdapter);
    }
}

