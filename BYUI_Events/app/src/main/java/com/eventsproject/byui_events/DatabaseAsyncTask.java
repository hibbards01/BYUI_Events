package com.eventsproject.byui_events;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Used to set up MainActivity with the tabs, but makes sure the database is filled before setting up the tabs.
 * Created by Grant on 12/9/2014.
 */
public class DatabaseAsyncTask extends AsyncTask<SQLDatabase, Object, Object> {
    MainActivity activity;
    ProgressDialog progressDialog;

    public DatabaseAsyncTask(MainActivity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
    }

    /**
     * Sets up the ProgressDialog
     */
    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Updating Events");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Writes to the database on another thread
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(SQLDatabase[] params) {
        params[0] = new SQLDatabase();
        params[0].run();

        return true;
    }

    /**
     * Gets rid of the ProgressDialog and sets up the Tabs with their necessary information
     * @param result
     */
    @Override
    protected void onPostExecute(Object result) {
        progressDialog.dismiss();

        // Create tabs!
        TabHost tabHost = (TabHost) activity.findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("DayTab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("WeekTab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("MonthTab");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("MyEventsTab");

        // Set up tab name and activity
        tab1.setIndicator("Day");
        tab1.setContent(new Intent(activity, DayActivity.class));

        tab2.setIndicator("Week");
        tab2.setContent(new Intent(activity, WeekActivity.class));

        tab3.setIndicator("Month");
        tab3.setContent(new Intent(activity, MonthActivity.class));

        tab4.setIndicator("My Events");
        tab4.setContent(new Intent(activity, MyEventsActivity.class));

        // Now add to the host!
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);

        // Now change the indicator color!
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView textView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            textView.setTextColor(activity.getResources().getColor(R.color.white));
            textView.setTextSize(15);
        }

        // Middle tab (which is week) is the current tab
        tabHost.setCurrentTab(1);
    }
}
