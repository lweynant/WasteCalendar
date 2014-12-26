package com.lweynant.wastecalendar.ui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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

public class WasteListActivity extends Activity
		implements
			UpcomingWasteListFragment.Callbacks,
			PastWasteListFragment.Callbacks {


	private static final String TAB = "selected-tab";

	private static final String TAG = "WasteListActivity";
	public static class TabListener<T extends Fragment>
			implements
				ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Fragment preInitializedFragment = (Fragment) mActivity
					.getFragmentManager().findFragmentByTag(mTag);

			// Check if the fragment is already initialized
			if (mFragment == null && preInitializedFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else if (mFragment != null) {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			} else if (preInitializedFragment != null) {
				ft.attach(preInitializedFragment);
				mFragment = preInitializedFragment;
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Notice that setContentView() is not used, because we use the root
		// android.R.id.content as the container for each fragment

		// setup action bar for tabs
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tab = actionBar
				.newTab()
				.setText(R.string.upcoming)
				.setTabListener(
						new TabListener<UpcomingWasteListFragment>(this, "new",
								UpcomingWasteListFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.past)
				.setTabListener(
						new TabListener<PastWasteListFragment>(this,
								"upcoming", PastWasteListFragment.class));
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
			case R.id.menu_item_modifycalendar :
				startActivity(new Intent(this, ModifyCalendarActivity.class));
				return true;
			case R.id.menu_item_settings :
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
			case R.id.menu_item_reset :
				AlarmService.setAlarmRequest(this, Date.today());
				return true;
			default :
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
