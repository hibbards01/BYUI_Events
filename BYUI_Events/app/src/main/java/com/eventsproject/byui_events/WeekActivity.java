package com.eventsproject.byui_events;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
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

    private List<String> headerList = new ArrayList<String>();
    private Map<String, String> childList = new HashMap<String, String>();
    private List<byte[]> images = new ArrayList<byte[]>();
    private ExpandableListViewAdapter listAdapter;
    private ExpandableListView expListView;
    private TextView weekDateView;
    private Database database = Database.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        expListView = (ExpandableListView) findViewById(R.id.week_list);
        weekDateView = (TextView) findViewById(R.id.week_view);

        // Grab the current date!
        Date currentDate = new Date();
        String stringCurrentDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        String[] dateParts = stringCurrentDate.split("-");

        Calendar calendar = new GregorianCalendar(
                Integer.parseInt(dateParts[0]),
                Integer.parseInt(dateParts[1])-1,
                Integer.parseInt(dateParts[2]));

        // Find start of the week of current day
        calendar.add(Calendar.DAY_OF_MONTH, -(calendar.get(Calendar.DAY_OF_WEEK)-1));
        Date startDate = calendar.getTime();
        String stringStartDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);

        // Find end of the week of current day
        calendar.add(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_WEEK)+5);
        Date endDate = calendar.getTime();
        String stringEndDate = new SimpleDateFormat("yyy-MM-dd").format(endDate);

        // Select the events that fall under this time period
        database.selectEvents(stringStartDate, stringEndDate, headerList, childList, images);

        // Set the start and end dates as the title
        stringStartDate = dateFormat(stringStartDate);
        stringEndDate = dateFormat(stringEndDate);
        weekDateView.setText(stringStartDate + " to " + stringEndDate);

        // Now to put it on the screen!
        listAdapter = new ExpandableListViewAdapter(this, headerList, childList, images);

        // Now set it to the screen!
        expListView.setAdapter(listAdapter);
    }

    private String dateFormat(String textDate) {
        //create the variables!
        String date = "";
        String [] splitDate = textDate.split("-");
        String [] month = {
                "none", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        date = month[Integer.parseInt(splitDate[1])] + " " + splitDate[2] + " " + splitDate[0];

        return date;
    }
}

