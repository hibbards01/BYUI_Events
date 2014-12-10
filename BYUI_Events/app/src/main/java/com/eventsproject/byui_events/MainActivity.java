package com.eventsproject.byui_events;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.TabHost;
import android.content.Intent;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends TabActivity {
    /*
     * MEMBER VARIABLES
     */
    private Menu menu; //save the menu
    private SQLDatabase dataBaseHome;
    //private DatabaseHelper db = DatabaseHelper.newInstance(this);
    private Database db = null;

    /*
     * MEMBER METHODS
     */

    /**
     * ONCREATE
     *  This is when the application is first started.
     *      It will also create the tabs.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database database = Database.newInstance(this);
        db = Database.getInstance();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Events");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Thread thread = new Thread(new SQLDatabase(progressDialog));
        thread.start();

        //create tabs!

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("DayTab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("WeekTab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("MonthTab");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("MyEventsTab");

        //set up tab name and activity
        tab1.setIndicator("Day");
        tab1.setContent(new Intent(this, DayActivity.class));

        tab2.setIndicator("Week");
        tab2.setContent(new Intent(this, WeekActivity.class));

        tab3.setIndicator("Month");
        tab3.setContent(new Intent(this, MonthActivity.class));

        tab4.setIndicator("My Events");
        tab4.setContent(new Intent(this, MyEventsActivity.class));

        //now add to the host!
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);

        //now change the indicator color!
        for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView textView = (TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTextSize(15);
        }

        tabHost.setCurrentTab(1);

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }

    /**
     * ONCREATEOPTIONSMENU
     *  Create the options menu!
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;

        MenuItem searchBar = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchBar.getActionView();
        searchView.setQueryHint("Search");

        //now for the textview!

        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);


        if (searchPlate!=null) {
            searchPlate.setBackgroundColor(getResources().getColor(R.color.darkgray));

            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);

            if (searchText!=null) {
                searchText.setTextColor(getResources().getColor(R.color.white));
                searchText.setHintTextColor(getResources().getColor(R.color.white));
                searchText.setTextSize(15);
            }
        }

        return true;
    }

    /**
     * ONOPTIONSITEMSELECTED
     *  What item was selected in the menu!
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("HERE!", "HERE!");

        switch(id) {
            case R.id.action_settings:
                Toast.makeText(this, "Running settings", Toast.LENGTH_LONG).show();
                db.deleteEvents();
                return true;
            case R.id.action_search:
                Toast.makeText(this, "Running search", Toast.LENGTH_LONG).show();
                return true;
            case R.id.filter_activities_complete:
            case R.id.filter_activities_life_skills:
            case R.id.filter_activities_social:
            case R.id.filter_activities_sports:
            case R.id.filter_activities_talent:
            case R.id.filter_activities_wellness:
            case R.id.filter_alumni_or_reunion:
            case R.id.filter_broadcast_conference:
            case R.id.filter_concert:
            case R.id.filter_conference_workshop:
            case R.id.filter_devo_speeches:
            case R.id.filter_get_connected:
            case R.id.filter_graduation:
            case R.id.filter_performance_events:
            case R.id.filter_performing_visual_arts:
            case R.id.filter_reception_openhouse:
            case R.id.filter_reception_public:
            case R.id.filter_theatre: {
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                return true;
            }
            case R.id.action_clear_filters: {
                unCheckBoxes();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * UNCHECKBOXES
     *  Uncheck all the boxes.
     */
    public void unCheckBoxes() {
        //now go thru all the items in the menu!
        MenuItem item = menu.getItem(0);
        SubMenu sub = item.getSubMenu();
        int num = sub.size();
        //now loop thru each item!
        for (int i = 0; i < num - 1; i++) {
            //now grab it!
            MenuItem grabItem = sub.getItem(i);
            //now check it...
            if (grabItem.isChecked()) {
                //and change it back to false!
                grabItem.setChecked(false);
            }
        }
    }
}

