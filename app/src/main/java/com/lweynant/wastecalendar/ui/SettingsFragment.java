package com.lweynant.wastecalendar.ui;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.model.IPreferences;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private static final String TAG = "SettingsFragment";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs);
		PreferenceManager.getDefaultSharedPreferences(getActivity()).
		   registerOnSharedPreferenceChangeListener(this);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		updateLastAlarmSetPreferenceView();
	}
	@Override
	public void onDestroy(){
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		PreferenceManager.getDefaultSharedPreferences(getActivity()).
		   unregisterOnSharedPreferenceChangeListener(this);
		
	}
	

	private void updateLastAlarmSetPreferenceView() {
		Log.d(TAG, "updateLastAlarmSetPreferenceView");
		Preference p = findPreference(IPreferences.LAST_ALARM);

		String summary = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(IPreferences.LAST_ALARM, "No alarm set");
		p.setSummary(summary);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.d(TAG, "onSharedPreferenceChanged");
		if (IPreferences.LAST_ALARM.equals(key)){
		  updateLastAlarmSetPreferenceView();
		}		
	}

}
