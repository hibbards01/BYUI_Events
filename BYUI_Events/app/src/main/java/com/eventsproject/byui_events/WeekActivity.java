package com.eventsproject.byui_events;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekActivity extends Activity {

    private ExpandableListViewAdapter adapter;
    private ExpandableListView list;
    private DatabaseHelper db = DatabaseHelper.getInstance();
    private List<String> header = new ArrayList<String>();
    private Map<String, String> childList = new HashMap<String, String>();
    private TextView weekDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        list = (ExpandableListView) findViewById(R.id.week_list);
        weekDateView = (TextView) findViewById(R.id.week_view);

        //grab the date!
        Date date = new Date();
        String textDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        //now grab from the database!
        //db.getEvent(textDate, header, childList);

        //and grab the date so it can be at the title!
        //textDate = dateFormat(textDate);
        //dateView.setText(textDate);

        //now to put it on the screen!
        adapter = new ExpandableListViewAdapter(this, header, childList);

        //now set it to the screen!
        list.setAdapter(adapter);
    }

    private String dateFormat(String textDate) {
        //create the variables!
        String date = "";
        String [] splitDate = textDate.split("-");
        String [] month = {
                "none", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        return date;
    }
}

