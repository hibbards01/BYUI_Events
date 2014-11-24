package com.eventsproject.byui_events;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DayActivity extends ListActivity {

    private DatabaseHelper db = DatabaseHelper.getInstance();
    private List<Event> list;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //grab today's date!
        Date date = new Date();
        String textDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        Log.d("Day: ", textDate);

        //now grab from the database!
        list = db.getEvent(textDate);
        //now to put it on the screen!
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_day,
//                R.id.label, list);
//        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {

    }
}

