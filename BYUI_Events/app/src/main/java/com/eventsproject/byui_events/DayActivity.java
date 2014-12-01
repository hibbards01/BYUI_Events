package com.eventsproject.byui_events;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DayActivity extends ListActivity {

    private DatabaseHelper db = DatabaseHelper.getInstance();
    private List<String> headerList = new ArrayList<String>();
    private Map<String, String> childList = new HashMap<String, String>();
    private ExpandableListViewAdapter listAdapter;
    private ExpandableListView expListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        //grab today's date!
        Date date = new Date();
        expListView = (ExpandableListView) findViewById(R.id.dayList);

        //grab the date!
        String textDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        //now grab from the database!
        db.getEvent(textDate, headerList, childList);

        //now to put it on the screen!
        listAdapter = new ExpandableListViewAdapter(this, headerList, childList);

        //now set it to the screen!
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void onStart() {

    }


    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {

    }
}

