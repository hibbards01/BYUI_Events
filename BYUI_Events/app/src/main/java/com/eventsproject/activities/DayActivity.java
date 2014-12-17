package com.eventsproject.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eventsproject.byui_events.ActivityObserver;
import com.eventsproject.byui_events.Database;
import com.eventsproject.byui_events.ExpandableListViewAdapter;
import com.eventsproject.byui_events.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


public class DayActivity extends TemplateActivity implements ActivityObserver {
    private static ExpandableListViewAdapter listAdapter;
    private static ExpandableListView expListView;
    private static TextView textView;

    /*
     * MEMBER METHODS
     */

    /**
     * ONCREATE
     * Create the list and display it!
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Log.d("DAY: ", "Created!");
        expListView = (ExpandableListView) findViewById(R.id.dayList);
        textView = (TextView) findViewById(R.id.dayDate);


        setAdapter();
    }

    @Override
    protected void grabFromDatabase() {
        //grab the date!
        Log.d("Day: ", date.toString());
        stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        Log.d("Day: ", stringDate);

        //now grab from the database!
        database.selectEvents(stringDate, stringDate, headerList, childList, imageList, dateList);
    }

    @Override
    protected void setTitle() {
        //and grab the date so it can be at the title!
        stringDate = dateFormat(stringDate);
        Log.d("Day: ", stringDate);

        textView.setText(stringDate);
    }

    @Override
    protected void setUpExpandableListViewAdapter() {
        //now to put it on the screen!
        if (listAdapter == null) {
            listAdapter = new ExpandableListViewAdapter(this, headerList, childList, imageList, dateList, "DAY");
        } else {
            listAdapter.setLists(headerList, childList, imageList, dateList);
        }

        //now set it to the screen!
        expListView.setAdapter(listAdapter);
    }

    /**
     * ONRESUME
     * This will start the activity!
     */
    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    public void update() {
        setAdapter();
    }
}

