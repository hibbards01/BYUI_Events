package com.eventsproject.byui_events;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ExpandableListView;
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
    private Database db = null;
    private View search = null;

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

        DatabaseAsyncTask asyncTask = new DatabaseAsyncTask(this);

        asyncTask.execute(dataBaseHome);
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
        final Activity mainActivity = this;
        final MenuItem searchBar = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)searchBar.getActionView();

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

        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Search", "onQueryTextChange");
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Search", "onQueryTextSubmit");

                new SearchActivity(mainActivity, query);
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
                return true;
            }
        });

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

