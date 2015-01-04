package com.lweynant.wastecalendar.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lweynant.wastecalendar.AlarmService;
import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.DateFormatter;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.WasteEventKeyValues;
import com.lweynant.wastecalendar.provider.WasteEventFactory;

public class WasteListActivity extends FragmentActivity
        implements
        UpcomingWasteListFragment.Callbacks,
        PastWasteListFragment.Callbacks {


    private static final String TAB = "selected-tab";

    private static final String TAG = WasteListActivity.class.getSimpleName();
    MyAdapter mAdapter;
    ViewPager mPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
        });
        // setup action bar for tabs
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }
        };
        ActionBar.Tab tab = actionBar
                .newTab()
                .setText(R.string.upcoming)
                .setTabListener(tabListener);
        actionBar.addTab(tab);

        tab = actionBar
                .newTab()
                .setText(R.string.past)
                .setTabListener(tabListener);
        actionBar.addTab(tab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DateFormatter formatter = new DateFormatter(new Resources(this));
        getActionBar().setSubtitle(formatter.format(Date.today()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(TAB);
            getActionBar().setSelectedNavigationItem(index);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt(TAB, getActionBar().getSelectedNavigationIndex());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_waste_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_modifycalendar:
                startActivity(new Intent(this, ModifyCalendarActivity.class));
                return true;
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
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

    public static class MyAdapter extends FragmentPagerAdapter {
        static final String TAG = "MyAdapter";

        public MyAdapter( FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem" + position);
            if (position == 0) {
                return new UpcomingWasteListFragment();
            } else {
                return new PastWasteListFragment();
            }
        }

    }
}
