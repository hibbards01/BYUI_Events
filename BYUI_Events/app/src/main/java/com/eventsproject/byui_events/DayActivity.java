package com.eventsproject.byui_events;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DayActivity extends Activity {

    /*
     * MEMBER VARIABLES
     */
    private ExpandableListViewAdapter listAdapter = null;
    private ExpandableListView expListView;
    private TextView dateView;
    private Database database = Database.getInstance();
    private Date date;

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

        //grab the date!
        date = new Date();
        expListView = (ExpandableListView) findViewById(R.id.dayList);
        dateView = (TextView) findViewById(R.id.dayDate);

        setAdapter();
    }

    /**
     * SETADAPTER
     */
    private void setAdapter() {
        //grab the date!
        String textDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        //create the lists!
        List<String> headerList = new ArrayList<String>();
        Map<String, String> childList = new HashMap<String, String>();
        List<byte[]> images = new ArrayList<byte[]>();
        Map<String, String[]> dateList = new HashMap<String, String[]>();

        //now grab from the database!
        database.selectEvents(textDate, textDate, headerList, childList, images, dateList);

        //and grab the date so it can be at the title!
        textDate = dateFormat(textDate);
        dateView.setText(textDate);

        //now to put it on the screen!
        if (listAdapter == null) {
            listAdapter = new ExpandableListViewAdapter(this, headerList, childList, images, dateList, "DAY");
        } else {
            listAdapter.setLists(headerList, childList, images, dateList);
        }

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
                "none", "  Jan", "  Feb", "  Mar", "  Apr", "  May", "  Jun",
                "  Jul", "  Aug", "  Sep", "  Oct", "  Nov", "  Dec"
        };

        date = month[Integer.parseInt(splitDate[1])] + " " + splitDate[2] + " " + splitDate[0];

        return date;
    }

    /**
     * ONSTART
     *  This will start the activity!
     */
    @Override
    protected void onStart() {
        super.onStart();

        //now create a listener for the list!
        //this will only allow one thing to be selected!
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (previousItem != groupPosition) {
                    expListView.collapseGroup(previousItem);
                    previousItem = groupPosition;
                }
            }
        });

        //grab the image buttons!
        ImageButton back = (ImageButton) findViewById(R.id.day_back_button);
        ImageButton forward = (ImageButton) findViewById(R.id.day_for_button);

        //grab the dates!
        final Calendar calendar = Calendar.getInstance();

        //set the time
        calendar.setTime(date);

        //now create the listeners!
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);

                date = calendar.getTime();
                setAdapter();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);

                date = calendar.getTime();
                setAdapter();
            }
        });
   }
}

