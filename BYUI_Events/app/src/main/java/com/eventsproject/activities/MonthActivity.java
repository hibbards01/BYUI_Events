package com.eventsproject.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eventsproject.byui_events.ExpandableListViewAdapter;
import com.eventsproject.byui_events.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MonthActivity extends TemplateActivity implements ActivityObserver {
    private static ExpandableListViewAdapter listAdapter;
    private static ExpandableListView expListView;
    private static TextView textView;
    private static Date date = new Date();

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
        textView = (TextView) findViewById(R.id.monthView);

        //now set the adapter!
        setAdapter();
    }

    @Override
    protected void grabFromDatabase() {
        //grab the date!
        stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String endDate = grabEndDate(stringDate);

        //now grab from the database!
        database.selectEvents(stringDate, endDate, headerList, childList, imageList, dateList);
    }

    @Override
    protected void setTitle() {
        //and grab the date so it can be at the title!
        String textDate = dateFormat(stringDate);
        textView.setText(textDate);
    }

    @Override
    protected void setUpExpandableListViewAdapter() {
        //now to put it on the screen!
        if (listAdapter == null) {
            listAdapter = new ExpandableListViewAdapter(this, headerList, childList, imageList, dateList, "MONTH", null);
        } else {
            listAdapter.setLists(headerList, childList, imageList, dateList);
        }

        //now set it to the screen!
        expListView.setAdapter(listAdapter);
    }

    /**
     * ONRESUME
     *  This will start the activity!
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
        ImageButton back = (ImageButton) findViewById(R.id.month_back_button);
        ImageButton forward = (ImageButton) findViewById(R.id.month_for_button);

        //now create the listeners!
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String [] splitDate = stringDate.split("-");
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
                String [] splitDate = stringDate.split("-");
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

    /**
     * UPDATE
     */
    @Override
    public void update() {
        setAdapter();
    }
}
