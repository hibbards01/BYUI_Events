package com.eventsproject.byui_events;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DayActivity extends Activity implements GestureDetector.OnGestureListener {

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
    private GestureDetector gd;
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

        //create a new gesture!
        gd = new GestureDetector(this, this);

        //grab the date!
        date = new Date();
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
//        expListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
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
   }

    /**
     * ONFLING
     *  This will catch which way the persons swipes their finger!
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //define a sensitivity!
        Log.d("DAY_ACTIVITY: ", "FLING EVENT!");
        float sesitivity = 50;
        boolean dateShifted = false;
        Calendar calendar = Calendar.getInstance();

        //now check the swipe event!
        //swipe left!
        if (e1.getX() - e2.getX() > sesitivity) {
            //now move the date forward by one!
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);

            date = calendar.getTime();
            dateShifted = true;
        }
        //swipe right!
        else if (e2.getX() - e1.getX() > sesitivity) {
            //move the date back by one!
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);

            date = calendar.getTime();
            dateShifted = true;
        }

        //now grab the events!
        if (dateShifted) {
            //grab the date!
            String textDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

            //erase everything from the old lists!
            headerList.clear();
            childList.clear();
            images.clear();

            //now grab from the database!
            database.selectEvents(textDate, headerList, childList, images);

            //and grab the date so it can be at the title!
            textDate = dateFormat(textDate);
            dateView.setText(textDate);

            //now to put it on the screen!
            listAdapter.setLists(headerList, childList, images);

            //now set it to the screen!
            expListView.setAdapter(listAdapter);

            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gd.onTouchEvent(event);
    }

    /*************************************************
     * ALL THESE I HAVE TO IMPLEMENT.
     ************************************************/
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }
}

