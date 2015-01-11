package com.lweynant.wastecalendar.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lweynant.wastecalendar.AlarmService;
import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.DateFormatter;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.WasteEventKeyValues;
import com.lweynant.wastecalendar.provider.WasteEventFactory;


public class WasteListActivity extends FragmentActivity
        implements
        UpcomingWasteListFragment.Callbacks,
        PastWasteListFragment.Callbacks {


    private static final String CURRENT_CALLENDAR_VIEW = "current_callendar_view";

    private static final String TAG = WasteListActivity.class.getSimpleName();
    DrawerLayout mDrawerLayout;
    LinearLayout mDrawerContent;
    ListView mCalendarViewList;
    ListView mOptionsList;
    String[] mCalendarViewNames;
    String[] mOptions;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentCalendarView = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        ILocalizer localizer = new Resources(this);
        mCalendarViewNames = new String[]{localizer.string(R.string.upcoming),
                localizer.string(R.string.past)};
        mOptions = new String[]{localizer.string(R.string.modify_calendar), localizer.string(R.string.settings)};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerContent = (LinearLayout) findViewById(R.id.left_drawer);
        mCalendarViewList = (ListView) findViewById(R.id.calendar_view_list);
        mCalendarViewList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mCalendarViewNames));

        mCalendarViewList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCalendarListView(position);
            }
        });
        mOptionsList = (ListView) findViewById(R.id.option_list);
        mOptionsList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptions));

        mOptionsList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(WasteListActivity.this, ModifyCalendarActivity.class));
                } else {
                    startActivity(new Intent(WasteListActivity.this, SettingsActivity.class));
                }
                mDrawerLayout.closeDrawer(mDrawerContent);
            }
        });


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectCalendarListView(int position) {
        mCurrentCalendarView = position;
        String section = mCalendarViewNames[mCurrentCalendarView];
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(section);
        // Check if the fragment is already initialized
        ILocalizer localizer = new Resources(this);
        String title = localizer.string(R.string.app_name);
        if (position == 1) {
            title = localizer.string(R.string.past);
        }

        if (fragment == null) {
            Log.v(TAG, "fragment was not found for section: " + section);
            // If not, instantiate and add it to the activity
            if (position == 0) {
                fragment = new UpcomingWasteListFragment();
            } else {
                fragment = new PastWasteListFragment();
            }
        } else {
            Log.v(TAG, "Hurray fragment was found for section: " + section);
            //todo this actually never happens because nobody holds on to the fragment
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        // Highlight the selected item, update the title, and close the drawer
        mCalendarViewList.setItemChecked(position, true);
        setTitle(title);
        mDrawerLayout.closeDrawer(mDrawerContent);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerContent);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectCalendarListView(mCurrentCalendarView);
        DateFormatter formatter = new DateFormatter(new Resources(this));
        getActionBar().setSubtitle(formatter.format(Date.today()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            selectCalendarListView(savedInstanceState.getInt(CURRENT_CALLENDAR_VIEW));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt(CURRENT_CALLENDAR_VIEW, mCurrentCalendarView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (mCurrentCalendarView == 0) {
            inflater.inflate(R.menu.activity_waste_list, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        switch (item.getItemId()) {
            case R.id.menu_item_reset:
                AlarmService.setAlarmRequest(this, Date.today());
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWasteEventClicked(IWasteEvent wasteEvent) {
        Log.d(TAG, "onWasteEventClicked: " + wasteEvent);
        IntentKeyValues intentKeyValues = new IntentKeyValues(new Intent(this,
                WasteDetailsActivity.class));
        WasteEventKeyValues writer = new WasteEventKeyValues(
                new WasteEventFactory(new Resources(this)));
        writer.writeTo(wasteEvent, intentKeyValues);
        startActivity(intentKeyValues.intent());
    }

}
