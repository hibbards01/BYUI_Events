package com.eventsproject.byui_events;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

public class WeekActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String [] list = new String[] {
                "Linux Meeting", "Career Fair", "I-Connect", "Game Night",
                "Theater", "Stem Fair", "Basketball", "Baseball", "Softball",
                "Ping Pong", "Racket ball", "Car Show", "English", "Math",
                "Red Hat", "Jumping Ropes"
        };

        //now to put it on the screen!
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_week,
                R.id.label, list);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {

    }
}

