package com.eventsproject.byui_events;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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

    private ExpandableListViewAdapter listAdapter;
    private ExpandableListView expListView;
    private TextView weekDateView;
    private Database database = Database.getInstance();
    private Date currentDate;
    private String stringCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        expListView = (ExpandableListView) findViewById(R.id.week_list);
        weekDateView = (TextView) findViewById(R.id.week_view);
        currentDate = new Date();

        setAdapter();
    }

    /**
     * SETADAPTER
     *  Creates the list!
     */
    public void setAdapter() {
        // Grab the current date!
        stringCurrentDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
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

        //create the lists!
        List<String> headerList = new ArrayList<String>();
        Map<String, String> childList = new HashMap<String, String>();
        List<byte[]> images = new ArrayList<byte[]>();
        Map<String, String[]> dateList = new HashMap<String, String[]>();

        // Select the events that fall under this time period
        database.selectEvents(stringStartDate, stringEndDate, headerList, childList, images, dateList);

        // Set the start and end dates as the title
        stringStartDate = dateFormat(stringStartDate);
        stringEndDate = dateFormat(stringEndDate);
        weekDateView.setText(stringStartDate + " to " + stringEndDate);

        // Now to put it on the screen!
        listAdapter = new ExpandableListViewAdapter(this, headerList, childList, images, dateList, "WEEK");

        // Now set it to the screen!
        expListView.setAdapter(listAdapter);
    }

    /**
     * DATEFORMAT
     *  Grab the month!
     * @param textDate
     * @return
     */
    private String dateFormat(String textDate) {
        //create the variables!
        String date = "";
        String [] splitDate = textDate.split("-");
        String [] month = {
                "none", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
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
        ImageButton back = (ImageButton) findViewById(R.id.week_back_button);
        ImageButton forward = (ImageButton) findViewById(R.id.week_for_button);

        //now create the listeners!
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grab the date!
                String [] splitDate = stringCurrentDate.split("-");

                Calendar calendar = new GregorianCalendar(Integer.parseInt(splitDate[0]),
                        Integer.parseInt(splitDate[1]),
                        Integer.parseInt(splitDate[2]));

                //move back seven days!
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                currentDate = calendar.getTime();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grab the date!
                String [] splitDate = stringCurrentDate.split("-");

                Calendar calendar = new GregorianCalendar(Integer.parseInt(splitDate[0]),
                        Integer.parseInt(splitDate[1]),
                        Integer.parseInt(splitDate[2]));

                //move forward seven days!
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                currentDate = calendar.getTime();
            }
        });
    }
}

