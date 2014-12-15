package com.eventsproject.byui_events;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthActivity extends Activity {
    /*
     * MEMBER VARIABLES
     */
    private ExpandableListViewAdapter listAdapter = null;
    private ExpandableListView expListView;
    private TextView monthView;
    private Database database = Database.getInstance();
    private Date date;
    private String startDate;

    /*
     * MEMBER METHDOS
     */

    /**
     * ONCREATE
     *  Create the activity!
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        //create new list and textview!
        expListView = (ExpandableListView) findViewById(R.id.monthList);
        monthView = (TextView) findViewById(R.id.monthView);
        date = new Date();

        //now set the adapter!
        setAdapter();
    }

    /**
     * SETADAPTER
     */
    private void setAdapter() {
        //grab the date!
        startDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String endDate = grabEndDate(startDate);

        //create the lists!
        List<String> headerList = new ArrayList<String>();
        HashMap<String, String> childList = new HashMap<String, String>();
        List<byte[]> images = new ArrayList<byte[]>();
        Map<String, String[]> dateList = new HashMap<String, String[]>();

        //now grab from the database!
        database.selectEvents(startDate, endDate, headerList, childList, images, dateList);

        //and grab the date so it can be at the title!
        String textDate = dateFormat(startDate);
        monthView.setText(textDate);

        //now to put it on the screen!
        if (listAdapter == null) {
            listAdapter = new ExpandableListViewAdapter(this, headerList, childList, images, dateList, "MONTH");
        } else {
            listAdapter.setLists(headerList, childList, images, dateList);
        }

        //now set it to the screen!
        expListView.setAdapter(listAdapter);
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
        ImageButton back = (ImageButton) findViewById(R.id.month_back_button);
        ImageButton forward = (ImageButton) findViewById(R.id.month_for_button);

        //now create the listeners!
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String [] splitDate = startDate.split("-");
                int numMonth = Integer.parseInt(splitDate[1]) - 1;
                int numYear = Integer.parseInt(splitDate[0]);

                //minus one to month!
                numMonth -= 1;

                if (numMonth == -1) {
                    numMonth = 11;
                    numYear -= 1;
                }

                //Log.d("MonthBack: ", Integer.toString(numYear) + " " + Integer.toString(numMonth));
                Calendar calendar = new GregorianCalendar(numYear, numMonth, 1);
                //Log.d("MonthBack: ", calendar.toString());
                date = calendar.getTime();
                //Log.d("MonthBack: ", date.toString());
                setAdapter();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String [] splitDate = startDate.split("-");
                int numMonth = Integer.parseInt(splitDate[1]) - 1;
                int numYear = Integer.parseInt(splitDate[0]);

                //add one to the month!
                numMonth += 1;

                if (numMonth == 12) {
                    numMonth = 0;
                    numYear += 1;
                }

                Calendar calendar = new GregorianCalendar(numYear, numMonth, 1);
                date = calendar.getTime();
                Log.d("Monthfor: ", date.toString());
                setAdapter();
            }
        });
    }

    /**
     * DATEFORMAT
     *  Format the date.
     * @param date
     * @return
     */
    public String dateFormat(String date) {
        //split the string!
        String [] splitDate = date.split("-");
        String [] month = {
                "none", "  January", "  February", "  March", "  April", "  May", "  June",
                "  July", "  August", "  September", "  October", "  November", "  December"
        };

        String text = month[Integer.parseInt(splitDate[1])] + " " + splitDate[0];

        return text;
    }

    /**
     * GRABENDDATE
     *  Grab the month end date.
     * @param startDate
     * @return
     */
    public String grabEndDate(String startDate) {
        //make some variables
        String endDate;
        String [] splitDate = startDate.split("-");

        //grab the year, month, and day
        int year = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]) - 1;
        int day = Integer.parseInt(splitDate[2]);

        //now grab the calendar!
        Calendar calendar = new GregorianCalendar(year, month, day);
        int totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //now put it back into a String
        endDate = splitDate[0] + "-" + splitDate[1] + "-" + Integer.toString(totalDays);

        Log.d("EndDate: ", endDate);

        return endDate;
    }
}
